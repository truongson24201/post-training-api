package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaView;
import apisystem.posttraining.DTO.BehaviorScore.BScoreAdd;
import apisystem.posttraining.DTO.BehaviorScore.BScoreView;
import apisystem.posttraining.DTO.BehaviorScore.BehaviorScoreDTO;
import apisystem.posttraining.DTO.BehaviorScore.BehaviorScoreView;
import apisystem.posttraining.DTO.StudentDTO.StudentShort;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IBehaviorScoreService;
import apisystem.posttraining.service.IContextHolder;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@EnableCaching
@AllArgsConstructor
public class BehaviorScoreServiceImpl implements IBehaviorScoreService {
    private final BehaviorScoreRepository behaviorScoreRepository;
    private final StudentRepository studentRepository;
    private final BSheetRepository bSheetRepository;
    private final ModelMapper modelMapper;
    private final BCriteriaSubRepository bCriteriaSubRepository;
    private final BSPatternContentRepository bsPatternContentRepository;
    private final IContextHolder contextHolder;


//    @Cacheable("tutorials")
//    @Override
//    public ResponseEntity<?> getAllBehaviorScore(Long classId, Long semesterId) {
//        List<Student> students = studentRepository.findAllByClassId(classId);
//        List<BehaviorSheet> behaviorSheets = bSheetRepository.findAllSemesterAndStudents(semesterId, students);
//        List<BehaviorScoreView> behaviorScoreViews = behaviorSheets.stream()
//                .map(bs -> viewBehaviorScoreOneStudent(bs,behaviorScoreRepository.findAllBSheet(bs.getBSheetId())))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(behaviorScoreViews);
//    }
    @Override
    public ResponseEntity<?> getAllBehaviorScore(Long classId, Long semesterId, int index) {

        int pageSize = 1; // Số lượng sinh viên cần lấy ra mỗi lần

        PageRequest pageable = PageRequest.of(index - 1, pageSize);
        Page<Student> page = studentRepository.findByClassId(classId,pageable);
        if (page.isEmpty()) return ResponseEntity.badRequest().body("Empty!");
//        Page<Student> page = studentRepository.findAll(pageable);

        Student student = page.getContent().get(0);
        BehaviorSheet behaviorSheet = bSheetRepository.findSemesterAndStudent(semesterId,student.getStudentId());
        if (behaviorSheet == null) return ResponseEntity.badRequest().body("Không có phiếu điểm rèn luyện cho lớp này và học kỳ này");
//        List<Student> students = studentRepository.findAllByClassId(classId);
//        List<BehaviorSheet> behaviorSheets = bSheetRepository.findAllSemesterAndStudents(semesterId, students);

//        List<BehaviorScoreView> behaviorScoreViews = behaviorSheets.stream()
//                .map(bs -> viewBehaviorScoreOneStudent(bs,behaviorScoreRepository.findAllBSheet(bs.getBSheetId())))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(behaviorScoreViews);
        return ResponseEntity.ok(
                viewBehaviorScoreOneStudent(behaviorSheet,
                        behaviorScoreRepository.findAllBSheet(behaviorSheet.getBSheetId()))
        );
    }

    public BehaviorScoreView viewBehaviorScoreOneStudent(BehaviorSheet behaviorSheet,List<BehaviorScore> behaviorScores){
        BehaviorScoreView behaviorScoreView = new BehaviorScoreView();

        behaviorScoreView.setBSheetId(behaviorSheet.getBSheetId());
        behaviorScoreView.setStudent(modelMapper.map(behaviorSheet.getStudent(), StudentShort.class));
//        behaviorScoreView.setBehaviorScores(behaviorScores.stream()
//                .map(bs -> modelMapper.map(bs,BehaviorScoreDTO.class))
//                .collect(Collectors.toList()));

        behaviorScoreView.setBScoreContent(mapToBScoreView(behaviorSheet.getBSPattern(),behaviorScores));
        return behaviorScoreView;
    }

    private List<BScoreView> mapToBScoreView(BSPattern bsPattern,List<BehaviorScore> behaviorScores) {
        // Tạo một Map để nhóm các BehaviorScore theo bCriteriaId
        Map<BCriteria, List<BehaviorScore>> scoresByCriteria = behaviorScores.stream()
                .collect(Collectors.groupingBy(score -> score.getBCriteriaSub().getBCriteria()));
//        List<BSPatternContent> bsPatternContents = bsPatternContentRepository.findAllByBSPatternID(bsPattern.getBSPatternId());
        List<BSPatternContent> bsPatternContents = bsPatternContentRepository.findAllByBSPatternID(bsPattern.getBSPatternId());
        Map<Long, BSPatternContent> bsPatternContentMap = new HashMap<>();
        bsPatternContents.forEach(content -> bsPatternContentMap.put(content.getBCriteriaSub().getBCriteriaSubId(), content));

        // Tạo danh sách BScoreView từ Map đã nhóm
        return scoresByCriteria.entrySet().stream()
                .map(entry -> {
                    BScoreView bScoreView = new BScoreView();
                    BCriteriaView bCriteriaView = modelMapper.map(entry.getKey(), BCriteriaView.class);

                    List<BehaviorScoreDTO> behaviorScoreDTOs = entry.getValue().stream()
                            .map(score -> {
                                BehaviorScoreDTO dto = modelMapper.map(score, BehaviorScoreDTO.class);
//                                BSPatternContent bsPatternContent = bsPatternContentRepository.findOneMaxPoint(bsPattern.getBSPatternId(),score.getBCriteriaSub().getBCriteriaSubId());
//                                dto.getBCriteriaSub().setMaxPoint(bsPatternContent.getMaxPoint());
                                BSPatternContent bsPatternContent = bsPatternContentMap.get(score.getBCriteriaSub().getBCriteriaSubId());
                                if (bsPatternContent != null) {
                                    dto.getBCriteriaSub().setMaxPoint(bsPatternContent.getMaxPoint());
                                }
                                return dto;
                            })
                            .collect(Collectors.toList());

                    bScoreView.setBCriteria(bCriteriaView);
                    bScoreView.setBehaviorScores(behaviorScoreDTOs);
                    return bScoreView;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> addBehaviorScore(Long bSheetId, List<BScoreAdd> BScoreAdds) {
        BehaviorSheet behaviorSheet = bSheetRepository.findById(bSheetId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found behavior sheet with id = " + bSheetId));
        List<BehaviorScore> behaviorScores = behaviorScoreRepository.findAllBSheet(behaviorSheet.getBSheetId());
        Map<Long, BScoreAdd> bScoreAddMap = BScoreAdds.stream()
                .collect(Collectors.toMap(BScoreAdd::getBCriteriaSubId, Function.identity()));
        // Duyệt qua danh sách BehaviorScore và cập nhật điểm từ BScoreAdd tương ứng

        for (BehaviorScore behaviorScore : behaviorScores) {
            BScoreAdd bScoreAdd = bScoreAddMap.get(behaviorScore.getBCriteriaSub().getBCriteriaSubId());
            if (bScoreAdd != null) {
                behaviorScore.setSelfPoint(bScoreAdd.getSelfPoint());
                behaviorScore.setClassPoint(bScoreAdd.getClassPoint());
                behaviorScore.setAdvisorPoint(bScoreAdd.getAdvisorPoint());
            }
        }
        try {
            behaviorScoreRepository.saveAll(behaviorScores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Save behavior score failed!");
        }
        return ResponseEntity.ok("Save behavior score successfully!");
    }

    @Override
    public ResponseEntity<?> checkPatternIsBlock(Long bSPatternId) {
        if (bSheetRepository.checkPatternInBSheet(bSPatternId).isEmpty())
            return ResponseEntity.ok(true);
        else return ResponseEntity.ok(false);
    }
    // cua student
    @Override
    public ResponseEntity<?> viewBSheet(Long semesterId, String username) {
        BehaviorSheet behaviorSheet = bSheetRepository.findStudentAndSemester(semesterId, username);
        List<BehaviorScore> behaviorScores = behaviorScoreRepository.findAllBSheet(behaviorSheet.getBSheetId());
        return ResponseEntity.ok(viewBehaviorScoreOneStudent(behaviorSheet,behaviorScores));
    }
}
