package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.BehaviorPattern.*;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IBSContentService;
import apisystem.posttraining.service.IContextHolder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class  BSContentsServiceImpl implements IBSContentService {
    private final BCriteriaRepository bCriteriaRepository;
    private final BCriteriaSubRepository bCriteriaSubRepository;
    private final BSPatternContentRepository bsPatternContentRepository;
    private final BSPatternRepository bsPatternRepository;
    private final SemesterRepository semesterRepository;
    private final ModelMapper modelMapper;
    private final SchedulingServiceImpl schedulingService;
    private final IContextHolder contextHolder;
    private final StudentRepository studentRepository;
    private final BSheetRepository bSheetRepository;
    private final BehaviorScoreRepository behaviorScoreRepository;


    @Override
    public ResponseEntity<?> addBCriteria(Object name) {
        BCriteria bCriteria = new BCriteria(name.toString());
        try {
//            BCriteria bCriteria = new BCriteria()
            bCriteria = bCriteriaRepository.save(bCriteria);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tạo tiêu chí thất bại");
        }
        BCriteriaView bCriteriaView = modelMapper.map(bCriteria,BCriteriaView.class);
        bCriteriaView.setStatus(true);
        return new ResponseEntity<>(bCriteriaView, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> addBCriteriaSub(BCriteriaSubAdd bCriteriaSubAdd) {
        BCriteria bCriteria = bCriteriaRepository.findById(bCriteriaSubAdd.getBCriteriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Behavior Criteria with id = " + bCriteriaSubAdd.getBCriteriaId()));
        BCriteriaSub bCriteriaSub = new BCriteriaSub(bCriteriaSubAdd.getName(),bCriteriaSubAdd.getDescription(),bCriteria);
        try {
            bCriteriaSub = bCriteriaSubRepository.save(bCriteriaSub);
            BCriteriaSubView bCriteriaSubView = modelMapper.map(bCriteriaSub, BCriteriaSubView.class);
            bCriteriaSubView.setStatus(true);
            return new ResponseEntity<>(bCriteriaSubView, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>("Tạo tiêu chí con thành công", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getAllBCriteria() {
        return ResponseEntity.ok(bCriteriaRepository.findAll().stream()
                .map(bCriteria -> {
                    boolean status = bCriteria.getBCriteriaSubs().stream()
                            .anyMatch(criteriaSub -> !criteriaSub.getBSPContents().isEmpty());

                    BCriteriaView bCriteriaView = new BCriteriaView();
                    bCriteriaView.setBCriteriaId(bCriteria.getBCriteriaId());
                    bCriteriaView.setName(bCriteria.getName());
                    bCriteriaView.setStatus(!status);

                    return bCriteriaView;
                })
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getAllBCriteriaSub(Long bCriteriaId) {
        return ResponseEntity.ok(bCriteriaSubRepository.findAllByBCriteriaId(bCriteriaId).stream()
                        .map(bCriteriaSub -> new BCriteriaSubView(bCriteriaSub.getBCriteriaSubId(),
                                bCriteriaSub.getName(),
                                bCriteriaSub.getDescription(),
                                bCriteriaSub.getBSPContents().isEmpty()))
                        .collect(Collectors.toList())
        );
    }

    public BSPattern addBSPattern() {
        return bsPatternRepository.save(new BSPattern(null, false,LocalDate.now(),""));
    }

    @Override
    public ResponseEntity<?> addBSPattern(List<BSContentAdd> bSContentAdd) {
        List<BSPatternContent> bsPatternContents;
        BSPattern bsPattern = bsPatternRepository.save(new BSPattern(null, false,LocalDate.now(),contextHolder.getAccount().getUsername()));
        bsPatternContents = bSContentAdd.stream()
                .map(bsContentAdd -> {
                    BCriteriaSub bCriteriaSub = bCriteriaSubRepository.findById(bsContentAdd.getBCriteriaSubId()).orElse(null);
                    if (bCriteriaSub != null) {
                        return new BSPatternContent(bsPattern, bCriteriaSub, bsContentAdd.getMaxPoint());
                    } else {
                        throw new ResourceNotFoundException("Not found Behavior criteria subs with id = " + bsContentAdd.getBCriteriaSubId());
                    }
                })
                .collect(Collectors.toList());
        try {
            bsPatternContentRepository.saveAll(bsPatternContents);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tạo mẫu điểm rèn luyện thất bại");
        }
        return new ResponseEntity<>("Tạo mẫu điểm rèn luyện thành công!",
                HttpStatus.CREATED);
    }

    @Transactional
    public void openBSFeature(Semester semester, BSPattern bsPattern){
        List<BehaviorSheet> behaviorSheets = studentRepository.findAll().stream()
                .map(student -> new BehaviorSheet(student,semester, bsPattern)).collect(Collectors.toList());
        try {
            behaviorSheets = bSheetRepository.saveAll(behaviorSheets);
            createBehaviorScore(behaviorSheets,bsPattern);
            return;
        }catch (Exception e){
            return;
        }
//        }
    }

    @Transactional
    public void createBehaviorScore(List<BehaviorSheet> behaviorSheets,BSPattern finalBsPattern){
        List<BCriteriaSub> criteriaSubs = finalBsPattern.getBsPatternContents().stream()
                .map(BSPatternContent::getBCriteriaSub)
                .collect(Collectors.toList());
        List<BehaviorScore> behaviorScores = behaviorSheets.stream()
                .flatMap(sheet -> criteriaSubs.stream()
                        .map(criteriaSub -> new BehaviorScore(sheet,criteriaSub)))
                .collect(Collectors.toList());
        try {
            behaviorScoreRepository.saveAll(behaviorScores);
        }catch (Exception e){
            return;
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateBSPattern(Long bSPatternId) {
        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
        BSPattern bsPattern = bsPatternRepository.findById(bSPatternId).get();
        List<BehaviorSheet> behaviorSheet = bSheetRepository.findFirstBySemesterIs(semester.getSemesterId());
        if (!bsPattern.getStatus() && behaviorSheet.isEmpty()){
            List<BehaviorSheet> behaviorSheets = studentRepository.findAll().stream()
                    .map(student -> new BehaviorSheet(student,semester, bsPattern)).collect(Collectors.toList());
            try {
                behaviorSheets = bSheetRepository.saveAll(behaviorSheets);
                List<BCriteriaSub> criteriaSubs = bsPattern.getBsPatternContents().stream()
                        .map(BSPatternContent::getBCriteriaSub)
                        .collect(Collectors.toList());
                List<BehaviorScore> behaviorScores = behaviorSheets.stream()
                        .flatMap(sheet -> criteriaSubs.stream()
                                .map(criteriaSub -> new BehaviorScore(sheet,criteriaSub)))
                        .collect(Collectors.toList());
                behaviorScoreRepository.saveAll(behaviorScores);
            }catch (Exception e){

            }
        }else if(!behaviorSheet.isEmpty() && !behaviorSheet.get(0).getBSPattern().getBSPatternId().equals(bSPatternId)){
            return ResponseEntity.badRequest().body("Bật/tắt thất bại.Mẫu đang được áp dụng trong học kỳ này là mẫu số: "+behaviorSheet.get(0).getBSPattern().getBSPatternId());
        }
        LocalDate current = LocalDate.now();
        String mess = "";
        if (bsPattern.getStatus()){
            bsPattern.setDateClose(current);
            mess = "Đóng chức năng nhập điểm rèn luyện thành công";
        }else {
            bsPattern.setDateOpen(current);
            bsPattern.setDateClose(null);
            mess = "Mở chức năng nhập điểm rèn luyện thành công";
        }
        bsPattern.setStatus(!bsPattern.getStatus());
        bsPattern.setUpdateOn(LocalDate.now());
        bsPattern.setUpdateBy(contextHolder.getAccount().getUsername());
        try {
            bsPatternRepository.save(bsPattern);
            return ResponseEntity.ok(mess);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Lỗi cập nhật mẫu");
        }
    }

    // cu
    public ResponseEntity<?> updateBSPattern1(Long bSPatternId) {
        Semester semester = semesterRepository.findCurrentSemester(LocalDate.now());
        BSPattern bsPattern = bsPatternRepository.findById(bSPatternId).get();
        LocalDate current = LocalDate.now();
        if (bsPattern.getStatus() && bsPattern.getDateOpen() != null
                && (bsPattern.getDateOpen().isBefore(current) | bsPattern.getDateOpen().equals(current))
                && (bsPattern.getDateClose().isAfter(current)) | bsPattern.getDateClose().equals(current)){
            return ResponseEntity.badRequest().body("ERROR, The student training point function is in the process of opening");
        }else if (!bsPattern.getStatus()
                && bsPattern.getDateOpen() == null
                && (semester.getDateEnd().equals(current) | semester.getDateEnd().isBefore(current))){

            schedulingService.newSemesterAPIBoolean();
        }

        if (!bsPatternRepository.getAllByStatusTrue(bSPatternId).isEmpty()){
            return ResponseEntity.badRequest().body("ERROR, There is already a behavior pattern enabled");
        }
        bsPattern.setStatus(!bsPattern.getStatus());
//        bsPattern.setDateOpen(semester.getDateEnd().plusDays(1));
//        bsPattern.setDateClose(semester.getDateEnd().plusWeeks(2));
        bsPattern.setUpdateOn(LocalDate.now());
        bsPattern.setUpdateBy("Anonymous");
        try {
            bsPatternRepository.save(bsPattern);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Turn on this behavior pattern failed!");
        }
        return ResponseEntity.ok("Turn on this behavior pattern successfully!");
    }

    @Override
    public ResponseEntity<?> getAllBSPatternContent() {
        return ResponseEntity.ok(bCriteriaRepository.findAll().stream().
                map(bsub -> modelMapper.map(bsub, BSContentView.class)).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> getBCriteriaDetails(Long criteriaId) {
        BCriteria bCriteria = bCriteriaRepository.findById(criteriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found criteria"));
        return ResponseEntity.ok(bCriteria);
    }

    @Override
    public ResponseEntity<?> getCriteriaSubDetails(Long criteriaSubId) {
        BCriteriaSub bCriteriaSub = bCriteriaSubRepository.findById(criteriaSubId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found criteria"));
        return ResponseEntity.ok(modelMapper.map(bCriteriaSub,BCriteriaSubView.class));
    }

    @Override
    public ResponseEntity<?> getAllBSPattern() {
        System.out.println(contextHolder.getRoleFromContext());
//        AtomicInteger counter = new AtomicInteger(1);
        return ResponseEntity.ok(bsPatternRepository.findAll().stream()
                .map(bsPattern -> {
                    BSPatternDTO bsPatternDTO = modelMapper.map(bsPattern, BSPatternDTO.class);
//                    bsPatternDTO.setOrdinal(counter.incrementAndGet());
                    return bsPatternDTO;
                })
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<?> editBCriteria(BCriteriaView criteria) {
        BCriteria bCriteria = bCriteriaRepository.findById(criteria.getBCriteriaId())
                .orElseThrow(()-> new ResourceNotFoundException("Not found bCriteria"));
        bCriteria.setName(criteria.getName());
        try {
            bCriteriaRepository.save(bCriteria);
            BCriteriaView bCriteriaView = modelMapper.map(bCriteria,BCriteriaView.class);
            bCriteriaView.setStatus(true);
            return ResponseEntity.ok(bCriteriaView);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cập nhật tiêu chí thành công");
        }
    }

    @Override
    public ResponseEntity<?> deleteBCriteria(Long criteriaId) {
        try {
            bCriteriaRepository.deleteById(criteriaId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Xóa tiêu chí thất bại");
        }
        return ResponseEntity.ok("Xóa tiêu chí thành công");
    }

    @Override
    public ResponseEntity<?> editBCriteriaSub(BCriteriaSubView bCriteriaSub) {
        BCriteriaSub criteriaSub = bCriteriaSubRepository.findById(bCriteriaSub.getBCriteriaSubId())
                .orElseThrow(()-> new ResourceNotFoundException("Not found bCriteria Sub"));
        criteriaSub.setName(bCriteriaSub.getName());
        criteriaSub.setDescription(bCriteriaSub.getDescription());
        try {
            bCriteriaSubRepository.save(criteriaSub);
            BCriteriaSubView bCriteriaSubView = modelMapper.map(criteriaSub,BCriteriaSubView.class);
            bCriteriaSubView.setStatus(true);
            return ResponseEntity.ok(bCriteriaSubView);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cập nhật tiêu chí con thất bại");
        }
    }

    @Override
    public ResponseEntity<?> deleteBCriteriaSub(Long bCriteriaSubId) {
        try {
            bCriteriaSubRepository.deleteById(bCriteriaSubId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Xóa tiêu chí con thất bại");
        }
        return ResponseEntity.ok("Xóa tiêu chí con thành công");
    }


    public List<BSContentView> convertToBSContentView(List<BSPatternContent> bsPatternContents) {
        Map<BCriteria, List<BCriteriaSubView>> criteriaToSubViewsMap = bsPatternContents.stream()
                .collect(Collectors.groupingBy(
                        bsPatternContent -> bsPatternContent.getBCriteriaSub().getBCriteria(),
                        Collectors.mapping(bsPatternContent -> {
                                    BCriteriaSubView dto = modelMapper.map(bsPatternContent.getBCriteriaSub(), BCriteriaSubView.class);
                                    dto.setMaxPoint(bsPatternContent.getMaxPoint());
                                    return dto;
                                },
                                Collectors.toList()
                        )
                ));

        List<BSContentView> bsContentViews = new ArrayList<>();
        criteriaToSubViewsMap.forEach((bCriteria, bCriteriaSubViews) -> {
            BSContentView bsContentView = new BSContentView();
            bsContentView.setBCriteriaId(bCriteria.getBCriteriaId());
            bsContentView.setName(bCriteria.getName());
            bsContentView.setBCriteriaSubs(bCriteriaSubViews);
            bsContentViews.add(bsContentView);
        });

        return bsContentViews;
    }

    @Override
    public ResponseEntity<?> getBSPatternContentDetails(Long bSPatternId) {
        List<BSPatternContent> bsPatternContents = bsPatternContentRepository.findAllByBSPatternID(bSPatternId);
        return ResponseEntity.ok(convertToBSContentView(bsPatternContents));
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateContentDetails(Long bSPatternId, List<BSContentAdd> bSContentAdd) {
        BSPattern bsPattern = bsPatternRepository.findById(bSPatternId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found this Pattern!"));
        List<BSPatternContent> bsPatternContents = bsPatternContentRepository.findAllByBSPatternID(bSPatternId);
        List<BSPatternContent> copy = new ArrayList<>(bsPatternContents);
        Map<Long, Integer> maxPointMap = bSContentAdd.stream()
                .collect(Collectors.toMap(
                        BSContentAdd::getBCriteriaSubId,
                        BSContentAdd::getMaxPoint
                ));
        Set<Long> updatedBCriteriaSubIds = new HashSet<>();
        Iterator<BSPatternContent> iterator = copy.iterator();
        while (iterator.hasNext()) {
            BSPatternContent bsPatternContent = iterator.next();
            Long bCriteriaSubId = bsPatternContent.getBCriteriaSub().getBCriteriaSubId();
            Integer maxPoint = maxPointMap.get(bCriteriaSubId);
            if (maxPoint != null) {
                bsPatternContent.setMaxPoint(maxPoint);
                updatedBCriteriaSubIds.add(bCriteriaSubId); // Thêm bCriteriaSubId đã được cập nhật vào Set
                maxPointMap.remove(bCriteriaSubId); // Xóa maxPointMap có key là bCriteriaSubId
            } else {
                iterator.remove(); // Xóa BSPatternContent không có maxPoint trong maxPointMap
            }
        }
        List<BSPatternContent> bsPatternContentsToDelete = bsPatternContents.stream()
                .filter(patternContent -> !updatedBCriteriaSubIds.contains(patternContent.getBCriteriaSub().getBCriteriaSubId()))
                .collect(Collectors.toList());
        if (updatedBCriteriaSubIds.size() != bsPatternContents.size()){
            bsPatternContents.removeIf(patternContent ->
                    !updatedBCriteriaSubIds.contains(patternContent.getBCriteriaSub().getBCriteriaSubId())
            );
        }
        if (!maxPointMap.isEmpty()){
            maxPointMap.forEach((bCriteriaSubId, maxPoint) -> {
                BSPatternContent newPatternContent = new BSPatternContent(bsPattern,
                        bCriteriaSubRepository.findById(bCriteriaSubId).get(),
                        maxPoint);
                bsPatternContents.add(newPatternContent); // Thêm BSPatternContent mới vào danh sách bsPatternContents
            });
        }
        try {
            bsPatternContentRepository.saveAll(bsPatternContents);
            bsPatternContentRepository.deleteAll(bsPatternContentsToDelete);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cập nhật nội dung cho mẫu thất bại");
        }
        return ResponseEntity.ok("Cập nhật nội dung cho mẫu thành công");
    }

    @Override
    public ResponseEntity<?> deleteContentDetails(Long bSPatternId) {
        BSPattern bsPattern = bsPatternRepository.findById(bSPatternId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found this Pattern!"));
        List<BSPatternContent> bsPatternContents = bsPatternContentRepository.findAllByBSPatternID(bSPatternId);
        try {
            bsPatternContentRepository.deleteAll(bsPatternContents);
            bsPatternRepository.deleteById(bSPatternId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Xóa mẫu thất bại");
        }
        return ResponseEntity.ok("Xóa mẫu thành công");
    }


}
