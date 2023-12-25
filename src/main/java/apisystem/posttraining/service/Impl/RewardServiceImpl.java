package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.Reward.*;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.ERDType;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IRewardService;
import apisystem.posttraining.utils.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements IRewardService {
    private final RewardsStudentsRepository rewardsStudentsRepository;
    private final FacultyRepository facultyRepository;
    private final SemesterRepository semesterRepository;
    private final StudentsScoresRepository studentsScoresRepository;
    private final BehaviorScoreRepository behaviorScoreRepository;
    private final ClassRepository classRepository;
    private final CurriculumRepository curriculumRepository;
    private final CreditsStudentsRepository creditsStudentsRepository;
    private final ClassCreditRepository classCreditRepository;
    private final ModelMapper modelMapper;
    private final BSheetRepository bSheetRepository;
    private final StudentRepository studentRepository;
    private final RewardDisciplineRepository rewardDisciplineRepository;

    @Override
    public ResponseEntity<?> previewRewardsStudents(RewardPreview request) {
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy ngành"));
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy học kỳ"));
//        Curriculum curriculum = curriculumRepository.findByFacultyAndSemester(faculty.getFacultyId(),semester.getSemesterId());
//        List<Subject> subjects = curriculum.getSubjects();
//        if(subjects.isEmpty()) return ResponseEntity.badRequest().body("Không có môn học");
//        List<ClassCredit> classCredits = classCreditRepository.findAllBySubjects(subjects);
        List<ClassCredit> classCredits = classCreditRepository.findAllByFacultyIdAndSemester(faculty.getFacultyId(),semester.getSemesterId());
        if (classCredits.isEmpty()) return ResponseEntity.badRequest().body("Không có lớp nào!");
//        Map<String, List<Long>> map = new HashMap<>();
        List<ClassCreditsStudents> allStudents = classCredits.stream()  // Giả sử classes là danh sách các ClassCredit
                .flatMap(classCredit -> classCredit.getStudents().stream())  // Lấy danh sách students từ mỗi ClassCredit
                .collect(Collectors.toList());

        Map<String, Map<Long, List<StudentsScores>>> studentScoresMap = new HashMap<>();

        List<Long> regisIds = allStudents.stream()
                .filter(ClassCreditsStudents::getStatus)
                .map(ClassCreditsStudents::getId).collect(Collectors.toList());

        List<StudentsScores> studentsScores = studentsScoresRepository.findAllByRegisIDs(regisIds);

        Map<Long, List<StudentsScores>> pointOneSubject = new HashMap<>();
        studentsScores.forEach(sS -> {
            pointOneSubject.computeIfAbsent(sS.getRegisClass().getId(), k -> new ArrayList<>())
                    .add(sS);
        });

        for (StudentsScores studentScore : studentsScores) {
            String studentId = studentScore.getRegisClass().getStudent().getStudentId();
            Long regisId = studentScore.getRegisClass().getId();
            // Kiểm tra xem Map chính có chứa StudentId này hay không
            if (!studentScoresMap.containsKey(studentId)) {
                studentScoresMap.put(studentId, new HashMap<>());
            }
            // Lấy Map con của StudentId này
            Map<Long, List<StudentsScores>> innerMap = studentScoresMap.get(studentId);
            // Kiểm tra xem Map con có chứa regisId này hay không
            if (!innerMap.containsKey(regisId)) {
                innerMap.put(regisId, new ArrayList<>());
            }
            // Thêm điểm số vào danh sách tương ứng trong Map con
            innerMap.get(regisId).add(studentScore);
        }

        Map<String, GpaReward> studentTotalScores = new HashMap<>();

        for (Map.Entry<String, Map<Long, List<StudentsScores>>> entry : studentScoresMap.entrySet()) {
            String studentId = entry.getKey();
            Map<Long, List<StudentsScores>> regisScoresMap = entry.getValue();
            BehaviorSheet behaviorSheet = bSheetRepository.findSemesterAndStudent(semester.getSemesterId(),studentId);
            // Tính tổng điểm của sinh viên từ danh sách điểm số của họ
            double totalTen = 0.0;
            double totalFound = 0.0;
            int i = 0;
            boolean failed = false;
            int totalCreditRegis = 0;
            // Duyệt qua từng môn của 1 sinh viên
            for (List<StudentsScores> scores : regisScoresMap.values()) {
                if (!scores.get(0).getRegisClass().getStatus()){
                    failed = true;
                    break;
                }
                totalCreditRegis += scores.get(0).getRegisClass().getClassCredit().getSubject().getCreditNum();
                if (creditsStudentsRepository.findAllByNotInSemesterOfStudent(studentId,
                        scores.get(0).getRegisClass().getClassCredit().getSubject().getSubjectId(),
                        semester.getSemesterId())) {
                    failed = true;
                    break;
                }
                double calTenPointOneSubject1 = CommonService.calTenPointOneSubject1(scores);
                double calFoundPoint = CommonService.convertTenToFour(calTenPointOneSubject1);
                if (calFoundPoint == 0.0){
                    failed = true;
                    break;
                }
                totalTen += calTenPointOneSubject1;
                totalFound += calFoundPoint;
                i++;
            }
            if (failed || totalCreditRegis < 16) continue;
            double averageFound = totalFound/i;
            if (averageFound >= request.getScoreMin()){
                int totalBehaviorPoint = calTotalBehaviorScoreOneSheet(behaviorSheet);
                if (totalBehaviorPoint >= request.getBehaviorMin()){
                    double averageTen = totalTen/i;
                    GpaReward gpaReward = new GpaReward(averageTen,averageFound,totalBehaviorPoint);
                    // Lưu tổng điểm vào Map của tổng điểm sinh viên
                    CommonService.calRewardRate(gpaReward);
                    studentTotalScores.put(studentId, gpaReward);
                }
            }
        }


        List<Map.Entry<String, GpaReward>> sortedList = studentTotalScores.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    GpaReward gpaReward1 = entry1.getValue();
                    GpaReward gpaReward2 = entry2.getValue();

                    if (!gpaReward1.getTotalFourPoint().equals(gpaReward2.getTotalFourPoint())) {
                        return Double.compare(gpaReward2.getTotalFourPoint(), gpaReward1.getTotalFourPoint());
                    } else if (!gpaReward1.getTotalTenPoint().equals(gpaReward2.getTotalTenPoint())) {
                        return Double.compare(gpaReward2.getTotalTenPoint(), gpaReward1.getTotalTenPoint());
                    } else {
                        return Integer.compare(gpaReward2.getTotalBehaviorPoint(), gpaReward1.getTotalBehaviorPoint());
                    }
                })
                .collect(Collectors.toList());


//        List<Map.Entry<String, GpaReward>> selectedStudents = sortedList.stream()
//                .limit(request.getAmount())
//                .collect(Collectors.toList());

//        List<RewardsStudents> rewardsStudents = new ArrayList<>();

//        RewardDiscipline rewardDiscipline = rewardDisciplineRepository.findByType(ERDType.reward);

        List<RewardsView> rewardsViewList = sortedList.stream()
                .map(entry -> {
                    RewardsView rewardsView = new RewardsView();
//                    Student student = studentRepository.findById(entry.getKey()).get();
//                    RewardsStudents reward = new RewardsStudents(rewardDiscipline,student,curriculum,entry.getValue().getTotalFourPoint(),entry.getValue().getTotalBehaviorPoint());
                    rewardsView.setStudent(modelMapper.map( studentRepository.findById(entry.getKey()).get(), StudentDTO.class));
                    rewardsView.setGpaReward(entry.getValue());
//                    rewardsStudents.add(reward);
                    return rewardsView;
                })
                .collect(Collectors.toList());

//        try {
//            rewardsStudentsRepository.saveAll(rewardsStudents);
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body("Lưu thất bại!");
//        }
//        if (rewardsViewList.isEmpty()) return ResponseEntity.ok("Không có sinh viên nào đạt điều kiện!");

        return ResponseEntity.ok(rewardsViewList);
    }

    @Override
    public ResponseEntity<?> addRewardsStudents(Long semesterId, List<RewardAdd> request) {
        Semester semester = semesterRepository.findById(semesterId).get();
        RewardDiscipline rewardDiscipline = rewardDisciplineRepository.findByType(ERDType.reward);
        List<RewardsStudents> rewardsViewList = request.stream()
                .map(rewardIterator -> {
                    Student student = studentRepository.findById(rewardIterator.getStudentId()).get();
                    return new RewardsStudents(rewardDiscipline,student,semester,rewardIterator.getGpaFound(),rewardIterator.getGpaBehavior(),rewardIterator.getContent());
                })
                .collect(Collectors.toList());
        try {
            rewardsStudentsRepository.saveAll(rewardsViewList);
            return ResponseEntity.ok("Lưu thành công!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Lưu thất bại!");
        }
    }

    @Override
    public ResponseEntity<?> getRewardsStudents(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId).get();
//        List<Curriculum> curriculum = curriculumRepository.findAllBySemester(semesterId);
        RewardDiscipline rewardDiscipline = rewardDisciplineRepository.findByType(ERDType.reward);
        List<RewardsStudents> rewardsStudents = rewardsStudentsRepository.findAllBySemester(semester.getSemesterId(), rewardDiscipline.getRewardDisciplineId());
        if (rewardsStudents.isEmpty()) return ResponseEntity.ok(new ArrayList<>());
//        List<RewardsView> rewardsViews = rewardsStudents.stream().map(rs -> {
//            GpaReward gpaReward = new GpaReward(rs.getGpaFound(), rs.getGpaBehavior(), rs.getContent());
//            return new RewardsView(modelMapper.map(rs.getStudent(), StudentDTO.class), gpaReward);
//        }).sorted(Comparator
//                .<RewardsView, Double>comparing(rv -> rv.getGpaReward().getTotalFourPoint(), Comparator.reverseOrder())
//                .thenComparing(rv -> rv.getGpaReward().getTotalTenPoint(), Comparator.reverseOrder())
//                .thenComparing(rv -> rv.getGpaReward().getTotalBehaviorPoint(), Comparator.reverseOrder())).collect(Collectors.toList());
//
//        return ResponseEntity.ok(rewardsViews);
        List<RewardsView> rewardsViews = rewardsStudents.stream()
                .map(rs -> {
                    GpaReward gpaReward = new GpaReward(
                            rs.getGpaFound(),
                            rs.getGpaBehavior(),
                            rs.getContent()
                    );
                    return new RewardsView(
                            modelMapper.map(rs.getStudent(), StudentDTO.class),
                            gpaReward
                    );
                })
                .sorted(
                        Comparator
                                .<RewardsView, Double>comparing(
                                        rv -> rv.getGpaReward().getTotalFourPoint(),
                                        Comparator.nullsLast(Comparator.reverseOrder())
                                )
                                .thenComparing(
                                        rv -> rv.getGpaReward().getTotalTenPoint(),
                                        Comparator.nullsLast(Comparator.reverseOrder())
                                )
                                .thenComparing(
                                        rv -> rv.getGpaReward().getTotalBehaviorPoint(),
                                        Comparator.nullsLast(Comparator.reverseOrder())
                                )
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(rewardsViews);
    }


    public int calTotalBehaviorScoreOneSheet(BehaviorSheet behaviorSheet){
        List<BehaviorScore> behaviorScores = behaviorScoreRepository.findAllBSheet(behaviorSheet.getBSheetId());
        return behaviorScores != null ?
                behaviorScores.stream()
                        .mapToInt(BehaviorScore::getAdvisorPoint)
                        .sum() : 0;
    }

    @Override
    public ResponseEntity<?> updateRewardsStudents(Long facultyId, Long semesterId, List<RewardAdd> request) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy ngành"));
        Semester semester = semesterRepository.findById(semesterId).get();
        RewardDiscipline rewardDiscipline = rewardDisciplineRepository.findByType(ERDType.reward);
        List<RewardsStudents> rewardsStudents = rewardsStudentsRepository.findAllByOneSemester(faculty.getFacultyId(),rewardDiscipline.getRewardDisciplineId(),semester.getSemesterId());
        List<RewardsStudents> rewardsViewList = request.stream()
                .map(rewardIterator -> {
                    Student student = studentRepository.findById(rewardIterator.getStudentId()).get();
                    return new RewardsStudents(rewardDiscipline,student,semester,rewardIterator.getGpaFound(),rewardIterator.getGpaBehavior(),rewardIterator.getContent());
                })
                .collect(Collectors.toList());
        try {
            rewardsStudentsRepository.deleteAll(rewardsStudents);
            rewardsStudentsRepository.saveAll(rewardsViewList);
            return ResponseEntity.ok("Lưu thành công!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Lưu thất bại!");
        }
    }

    @Override
    public boolean checkHasReward(Long facultyId, Long semesterId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy ngành"));
        Semester semester = semesterRepository.findById(semesterId).get();
        RewardDiscipline rewardDiscipline = rewardDisciplineRepository.findByType(ERDType.reward);
        List<RewardsStudents> rewardsStudents = rewardsStudentsRepository.findAllByOneSemester(faculty.getFacultyId(),rewardDiscipline.getRewardDisciplineId(),semester.getSemesterId());
        if (rewardsStudents.isEmpty()) return false;
        else return true;
    }
}
