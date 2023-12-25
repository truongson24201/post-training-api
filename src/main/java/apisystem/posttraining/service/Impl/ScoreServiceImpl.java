package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.SemesterDTO.SemesterDTO;
import apisystem.posttraining.DTO.StudentRegisDTO.StudentRegisView;
import apisystem.posttraining.DTO.StudentsScores.*;
import apisystem.posttraining.DTO.StudentsScores.Student.SemesterScoreView;
import apisystem.posttraining.DTO.StudentsScores.Student.SubjectPointView;
import apisystem.posttraining.DTO.SubjectDTO.SubjectView;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.ERole;
import apisystem.posttraining.exception.ResourceNotFoundException;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IContextHolder;
import apisystem.posttraining.service.IScoreService;
import apisystem.posttraining.utils.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements IScoreService {
    private final StudentsScoresRepository studentsScoresRepository;
    private final ComponentSubjectRepository componentSubjectRepository;
    private final CreditsStudentsRepository creditsStudentsRepository;
    private final ModelMapper modelMapper;
    private final SemesterRepository semesterRepository;
    private final IContextHolder contextHolder;
    private final ExamRepository examRepository;
    private final ComponentPointRepository componentPointRepository;
    private final TimeTableRepository timeTableRepository;
    private final ClassCreditRepository classCreditRepository;


    @Transactional
    @Override
    public ResponseEntity<?> updateStudentsScores(List<StudentsScoresAdd> studentsScoresAdd) {
//        List<Long> regisId = studentsScoresAdd.stream().map(StudentsScoresAdd::getRegisClassId).collect(Collectors.toList());
//        List<ClassCreditsStudents> creditsStudentss = creditsStudentsRepository.findAllByID(regisId);
        List<ClassCreditsStudents> creditsStudentss = new ArrayList<>();

        List<StudentsScores> studentsScores = studentsScoresAdd.stream()
                .flatMap(sSAdd -> {
                    ClassCreditsStudents creditsStudents = creditsStudentsRepository.findById(sSAdd.getRegisClassId()).get();
                    List<StudentsScores> sSList = studentsScoresRepository.findAllByRegisID(sSAdd.getRegisClassId());
                    sSList.forEach(sS -> {
                        sSAdd.getStudentPoints().forEach(studentPoint -> {
                            if (sS.getComponentSubject().getComSubId().equals(studentPoint.getComSubId()))
                                sS.setPointNumber(studentPoint.getPointNumber());
                        });
                    });
                    GPA gpa = new GPA(0.0, 0.0, "", false);
                    gpa.setTotalTenPoint(CommonService.calTenPointOneSubject1(sSList));
                    CommonService.convertTenToFour(gpa);
                    boolean hasZeroPoint = sSList.stream()
                            .anyMatch(score -> score.getPointNumber() == 0.0);
                    if (hasZeroPoint || !gpa.isResult())
                        creditsStudents.setStatus(false);
                    else  creditsStudents.setStatus(true);
//                    creditsStudentss.add(creditsStudents);
                    creditsStudentsRepository.save(creditsStudents);
                    return sSList.stream();
                })
                .collect(Collectors.toList());
//        List<StudentsScores> studentsScores = new ArrayList<>();
//        studentsScoresAdd.forEach(sSAdd -> {
//            ClassCreditsStudents creditsStudents = creditsStudentsRepository.findById(sSAdd.getRegisClassId()).get();
//            List<StudentsScores> sSList = studentsScoresRepository.findAllByRegisID(sSAdd.getRegisClassId());
//            sSList.forEach(sS -> {
//                sSAdd.getStudentPoints().forEach(studentPoint -> {
//                    if (sS.getComponentSubject().getComSubId().equals(studentPoint.getComSubId()))
//                        sS.setPointNumber(studentPoint.getPointNumber());
//                });
//            });
//            GPA gpa = new GPA(0.0, 0.0, "", false);
//            gpa.setTotalTenPoint(CommonService.calTenPointOneSubject1(sSList));
//            CommonService.convertTenToFour(gpa);
//            boolean hasZeroPoint = sSList.stream()
//                    .anyMatch(score -> score.getPointNumber() == 0.0);
//            if (hasZeroPoint || !gpa.isResult())
//                creditsStudents.setStatus(false);
//            else  creditsStudents.setStatus(true);
//            creditsStudentss.add(creditsStudents);
//            creditsStudentsRepository.save(creditsStudents);
////            studentsScores.addAll(sSList);
////            creditsStudentsRepository.updateStatusById(creditsStudents.getStatus(),creditsStudents.getId());
//            creditsStudentsRepository.flush();
//        });

        try {
            studentsScoresRepository.saveAll(studentsScores);
            creditsStudentsRepository.saveAll(creditsStudentss);
//            creditsStudentsRepository.clear();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Save scores failed!");
        }
        return ResponseEntity.ok("Save scores successfully!");
    }

    @Override
    public ResponseEntity<?> getStudentsScores(Long classCreditId) {
        // Phan quyen -> phong khao thi hien thi them cot cuoi ky
        // neu la giang vien -> check id class credit co phai giang vien dang dang nhap phu trach day hay khong
        List<Long> regisId = creditsStudentsRepository.findAllByClassCreditId(classCreditId)
                .stream().map(ClassCreditsStudents::getId).collect(Collectors.toList());
        if (regisId.isEmpty()) return ResponseEntity.ok(new ArrayList<>());
        // cho phong khao thi
        List<StudentsScores> studentsScores;
        if (contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.lecturer.name()))) {
            studentsScores = studentsScoresRepository.findAllNoFinalScore(regisId);
        } else {
            studentsScores = studentsScoresRepository.findAllByRegisIDs(regisId);
        }
        Map<Long, List<ComPointView>> studentPointsMap = new HashMap<>();
        studentsScores.forEach(sS -> {
            studentPointsMap.computeIfAbsent(sS.getRegisClass().getId(), k -> new ArrayList<>())
                    .add(modelMapper.map(sS, ComPointView.class));
        });
        studentPointsMap.forEach((key, value) ->
                value.sort(Comparator.comparing(
                        comPointView -> comPointView.getComponentSubject().getComponent().getComponentId()
                ))
        );
        return getResponseEntity(studentPointsMap);
    }

    @Override
    public ResponseEntity<?> getStudentScores(String studentId) {
        // lay ra thang sinh vien dang dang nhap
        List<Long> regisId = creditsStudentsRepository.findAllByStudentID(studentId)
                .stream().map(ClassCreditsStudents::getId).collect(Collectors.toList());
        if (regisId.isEmpty()) return ResponseEntity.ok(regisId);
        List<StudentsScores> studentsScores = studentsScoresRepository.findAllByRegisIDs(regisId);
        Map<Long, List<ComPointView>> pointOneSubject = new HashMap<>();
        studentsScores.forEach(sS -> {
            pointOneSubject.computeIfAbsent(sS.getRegisClass().getId(), k -> new ArrayList<>())
                    .add(modelMapper.map(sS, ComPointView.class));
        });


        Map<Long, List<SubjectPointView>> subjectsPoints = new HashMap<>();

        for (Map.Entry<Long, List<ComPointView>> entry : pointOneSubject.entrySet()) {
            GPA gpa = new GPA(0.0, 0.0, "", false);
            ClassCreditsStudents creditsStudents = creditsStudentsRepository.findById(entry.getKey()).get();
            gpa.setTotalTenPoint(CommonService.calTenPointOneSubject(entry.getValue()));
            CommonService.convertTenToFour(gpa);
            if (!creditsStudents.getStatus())
                gpa.setResult(false);
            SubjectPointView subjectPointView = new SubjectPointView(
                    modelMapper.map(creditsStudents.getClassCredit().getSubject(), SubjectView.class),
                    entry.getValue(),
                    gpa,
                    creditsStudents.getClassCredit().getClassCreditId());
            subjectsPoints.computeIfAbsent(creditsStudents.getClassCredit().getSemester().getSemesterId(), k -> new ArrayList<>())
                    .add(subjectPointView);
        }


        List<SemesterScoreView> semesterScoreViews = new ArrayList<>();

        for (Map.Entry<Long, List<SubjectPointView>> entry : subjectsPoints.entrySet()) {
            Semester semester = semesterRepository.findById(entry.getKey()).get();
            List<GPA> gpas = entry.getValue().stream().map(SubjectPointView::getGpa).collect(Collectors.toList());
            semesterScoreViews.add(new SemesterScoreView(
                    modelMapper.map(semester, SemesterDTO.class),
                    entry.getValue(),
                    CommonService.calSemesterAverageFour(gpas),
                    CommonService.calSemesterAverageTen(gpas),
                    CommonService.calSemesterCreditNum(entry.getValue())
            ));
        }

        Collections.sort(semesterScoreViews, new Comparator<SemesterScoreView>() {
            @Override
            public int compare(SemesterScoreView o1, SemesterScoreView o2) {
                int yearComparison = o1.getSemester().getYear().compareTo(o2.getSemester().getYear());
                if (yearComparison != 0) {
                    return yearComparison;
                } else {
                    return Integer.compare(o1.getSemester().getNum(), o2.getSemester().getNum());
                }
            }
        });

        double averageFour = 0.0;
        double averageTen = 0.0;
        int creditNum = 0;
        int numItems = 0;

        for (SemesterScoreView semesterScoreView : semesterScoreViews) {
            averageTen += semesterScoreView.getSemesterAverageTen();
            averageFour += semesterScoreView.getSemesterAverageFour();
            creditNum += semesterScoreView.getSemesterCreditNum();
            numItems++;
            semesterScoreView.setAverageTen(averageTen / numItems);
            semesterScoreView.setAverageFour(averageFour / numItems);
//            semesterScoreView.setCreditNum(creditNum / numItems);
            semesterScoreView.setCreditNum(creditNum);
        }

        return ResponseEntity.ok(semesterScoreViews);
    }

    @Override
    public ResponseEntity<?> getStudentsScoresWithExam(Long examId) {
        Exam exam = examRepository.findById(examId).get();
        List<Long> regisId = creditsStudentsRepository.findAllForExam(exam.getClassCredit().getClassCreditId(), exam.getStudents())
                .stream().map(ClassCreditsStudents::getId).collect(Collectors.toList());
        if (regisId.isEmpty()) return ResponseEntity.badRequest().body("Lớp tín chỉ không có sinh viên nào");
        if (contextHolder.getRoleFromContext().stream().anyMatch(r -> r.equals(ERole.lecturer.name()))) {
            return ResponseEntity.badRequest().body("Tài khoản không phải là phòng khảo thí -> không có quyền");
        }
        List<StudentsScores> studentsScores = studentsScoresRepository.findAllFinalScore(regisId);
        Map<Long, List<ComPointView>> studentPointsMap = new HashMap<>();
        studentsScores.forEach(sS -> {
            studentPointsMap.computeIfAbsent(sS.getRegisClass().getId(), k -> new ArrayList<>())
                    .add(modelMapper.map(sS, ComPointView.class));
        });
        return getResponseEntity(studentPointsMap);
    }

    private ResponseEntity<?> getResponseEntity(Map<Long, List<ComPointView>> studentPointsMap) {
        List<StudentScoreView> studentScoreViews = new ArrayList<>();
        for (Map.Entry<Long, List<ComPointView>> entry : studentPointsMap.entrySet()) {
            ClassCreditsStudents creditsStudents = creditsStudentsRepository.findById(entry.getKey()).get();
            studentScoreViews.add(new StudentScoreView(modelMapper.map(creditsStudents, StudentRegisView.class), entry.getValue()));
        }
        return ResponseEntity.ok(studentScoreViews);
    }

    @Override
    public ResponseEntity<?> saveStudentsExamStatus(List<UpExamStatus> upExamStatus) {
        Map<Long, Boolean> map2 = upExamStatus.stream()
                .collect(Collectors.toMap(UpExamStatus::getRegisClassId, UpExamStatus::isExamStatus));
        List<Long> regisId = upExamStatus.stream().map(UpExamStatus::getRegisClassId).collect(Collectors.toList());
        List<ClassCreditsStudents> students = creditsStudentsRepository.findAllByID(regisId); // Lấy danh sách từ cơ sở dữ liệu

        students.forEach(s -> {
            if (map2.containsKey(s.getId())) {
                boolean examStatus = map2.get(s.getId());
                s.setExamStatus(examStatus);
                if (!examStatus)
                    s.setStatus(false);
            }
        });
        try {
            creditsStudentsRepository.saveAll(students);
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cập nhật trạng thái thất bại");
        }
    }

    public Double calAttendancePoint(Long classCreditId){
        ClassCredit classCredit = classCreditRepository.findById(classCreditId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy"));
        Subject subject = classCredit.getSubject();
        ComponentPoint componentPoint = componentPointRepository.findByName("Chuyên cần");
        ComponentSubject componentSubject = componentSubjectRepository.findByAttendanceScore(componentPoint.getComponentId(), subject.getSubjectId());
        int period = subject.getCreditNum() * 15; // tiet hoc = tin chi * 5
        int lession = (int) Math.ceil((period / 4.0));
        return (10.0/lession*1.0);
    }

    @Override
    public ResponseEntity<?> getAttendancePoint(Long classCreditId){
        return ResponseEntity.ok(calAttendancePoint(classCreditId));
    }

    @Override
    public ResponseEntity<?> updateAttendanceScores(Long classCreditId) {
        // loc ra cac tkb ngay hom qua so voi ngay hien tai
//        Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
//        List<ClassCreditsStudents> creditsStudentsAll = new ArrayList<>();
        ComponentPoint componentPoint = componentPointRepository.findByName("attendance");
        List<TimeTable> timeTables = timeTableRepository.findByClassToCurrentDate(classCreditId,LocalDate.now());
        if (timeTables.isEmpty()) return ResponseEntity.badRequest().body("Chưa có buổi học nào được diễn ra");
        Double pointForLesson = 0.0D;
        List<ClassCreditsStudents> creditsStudents = null;
        if (!timeTables.isEmpty()) {
            ClassCredit classCredit = timeTables.get(0).getClassCredit();
            pointForLesson = calAttendancePoint(classCreditId);
            creditsStudents = creditsStudentsRepository.findAllByClassCreditId(classCredit.getClassCreditId());
            if (creditsStudents.isEmpty()) return ResponseEntity.badRequest().body("Lớp tín chỉ này không có sinh viên nào");
        }

        // Tạo một Map để lưu trữ tổng điểm của mỗi sinh viên
        Map<String, Double> totalScoresByStudent = new HashMap<>();

        for (TimeTable timeTable : timeTables){
            if (timeTable.getStudents().isEmpty()) {
                // Nếu danh sách sinh viên của timeTable là null, ghi điểm cho tất cả sinh viên của lớp
                for (ClassCreditsStudents ccs : creditsStudents){
//                    StudentsScores studentScore = studentsScoresRepository.findByRegisClass(ccs.getClassCredit().getClassCreditId());
//                    Double totalPoint = 0.0D;
////                            studentScore.setPointNumber(studentScore.getPointNumber() + pointForLesson); // old
//                    studentScore.setPointNumber(studentScore.getPointNumber() + pointForLesson);
                    double currentTotalScore = totalScoresByStudent.getOrDefault(ccs.getStudent().getStudentId(), 0.0);
                    currentTotalScore += pointForLesson;
                    totalScoresByStudent.put(ccs.getStudent().getStudentId(), currentTotalScore);
                }
            } else {
                // Nếu danh sách sinh viên của timeTable không phải là null, loại bỏ sinh viên nghỉ và ghi điểm cho sinh viên còn lại
//                creditsStudents.removeIf(creditsStudent -> timeTable.getStudents().contains(creditsStudent.getStudent()));1
                List<Student> attendedStudents = timeTable.getStudents();
                List<ClassCreditsStudents> remainingStudents = new ArrayList<>(creditsStudents);
                remainingStudents.removeIf(creditsStudent -> attendedStudents.contains(creditsStudent.getStudent()));

                for (ClassCreditsStudents ccs : remainingStudents){
//                    StudentsScores studentScore = studentsScoresRepository.findByRegisClass(ccs.getClassCredit().getClassCreditId());
//                    studentScore.setPointNumber(studentScore.getPointNumber() + pointForLesson);
                    double currentTotalScore = totalScoresByStudent.getOrDefault(ccs.getStudent().getStudentId(), 0.0);
                    currentTotalScore += pointForLesson;
                    totalScoresByStudent.put(ccs.getStudent().getStudentId(), currentTotalScore);
                }
            }
        }

        List<StudentsScores> studentsScores = new ArrayList<>();

        for (Map.Entry<String, Double> entry : totalScoresByStudent.entrySet()) {
            String studentId = entry.getKey();
            Double totalScore = entry.getValue();

            ClassCreditsStudents regisId = creditsStudentsRepository.findByStudent(classCreditId,studentId);
            StudentsScores studentScore = studentsScoresRepository.findStudentByAttendance(regisId.getId());
            if (studentScore != null) {
                studentScore.setPointNumber(totalScore);
                studentsScores.add(studentScore);
            }
        }

        try {
            studentsScoresRepository.saveAll(studentsScores);
            return ResponseEntity.ok("Ghi điểm chuyên cần thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ghi điểm chuyên cần lỗi");
        }
    }

    @Override
    public boolean checkExpiredToEnterPoint(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found semester!"));
        Semester currentSemester = semesterRepository.findCurrentSemester(LocalDate.now());
        if (semester.getSemesterId().equals(currentSemester.getSemesterId()))
            return false;
        if (Math.abs(currentSemester.getYear() - semester.getYear()) <= 1) {
            if (Math.abs(currentSemester.getYear() - semester.getYear()) == 1) {
                if (currentSemester.getNum() == 1 && (semester.getNum() == 2 | semester.getNum() == 3))
                    return false;
                else return true;
            } else {
               if (currentSemester.getNum() == 3 && semester.getNum() == 1)
                   return true;
               else return false;
            }
        }
        return true;
    }
}
