package apisystem.posttraining.service.Impl;

import apisystem.posttraining.entity.*;
import apisystem.posttraining.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class SchedulingServiceImpl{
    private final TimeTableRepository timeTableRepository;
    private final CreditsStudentsRepository creditsStudentsRepository;
    private final StudentsScoresRepository studentsScoresRepository;
    private final ClassCreditRepository classCreditRepository;
    private final ComponentSubjectRepository componentSubjectRepository;
    private final ComponentPointRepository componentPointRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final BSPatternRepository bsPatternRepository;
    private final StudentRepository studentRepository;
    private final BSheetRepository bSheetRepository;
    private final BehaviorScoreRepository behaviorScoreRepository;



    //    @Scheduled(fixedRate = 10000)
//    @Scheduled(cron = "0 0 0 1 * ?") // monthly
    @Transactional
    public void openBSFeature(Semester semester){
//        LocalDate current = LocalDate.now();
//        Semester semester = semesterRepository.findCurrentSemester(current);
//        if (semester.getDateEnd().getMonth() == current.getMonth()){
        // ban 1
            Optional<BSPattern> bsPattern = bsPatternRepository.findByStatusTrue();
            if (bsPattern.isEmpty()) bsPattern = bsPatternRepository.findFirstByDateOpenMax();

            BSPattern finalBsPattern = bsPattern.get();
            List<BehaviorSheet> behaviorSheets = studentRepository.findAll().stream()
                    .map(student -> new BehaviorSheet(student,semester, finalBsPattern)).collect(Collectors.toList());
            try {
                behaviorSheets = bSheetRepository.saveAll(behaviorSheets);
                updateBSPattern(bsPattern.get(),semester);
                createBehaviorScore(behaviorSheets,finalBsPattern);
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
    public void updateBSPattern(BSPattern bsPattern,Semester semester){
        bsPattern.setStatus(true);
        bsPattern.setDateOpen(semester.getDateEnd());
        bsPattern.setDateClose(semester.getDateEnd().plusWeeks(2));
        bsPattern.setUpdateOn(LocalDate.now());
        bsPattern.setUpdateBy("System");
        try {
            bsPatternRepository.save(bsPattern);
            return;
        }catch (Exception e){
            return;
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // daily
    @Transactional
    public void newSemester(){
        LocalDate curDate = LocalDate.now();
        Semester semester = semesterRepository.findCurrentSemester(curDate);
        if (semester.getDateEnd().equals(curDate) | semester.getDateEnd().isBefore(curDate)) {
            int tmpNum = 1;
            if(semester.getNum() != 3) tmpNum = semester.getNum() +1;
            try {
                openBSFeature(semester);
                semesterRepository.save(new Semester(curDate.getYear(),tmpNum,semester.getDateEnd().plusDays(1),semester.getDateEnd().plusMonths(3)));
            }catch (Exception e){
                return;
            }
        }
    }

    @Transactional
    public ResponseEntity<?> newSemesterAPI(){
        LocalDate curDate = LocalDate.now();
        Semester semester = semesterRepository.findCurrentSemester(curDate);
        if (semester.getDateEnd().equals(curDate) | semester.getDateEnd().isBefore(curDate)) {
            openBSFeature(semester);
            int tmpNum = 1;
            if(semester.getNum() != 3) tmpNum = semester.getNum() +1;
            try {
                semesterRepository.save(new Semester(curDate.getYear(),tmpNum,semester.getDateEnd().plusDays(1),semester.getDateEnd().plusMonths(3)));
                return ResponseEntity.ok("OK");
            }catch (Exception e){
                return ResponseEntity.badRequest().body("Error");
            }
        }
        return ResponseEntity.badRequest().body("It's not time to create a new Semester yet");
    }


    public boolean newSemesterAPIBoolean(){
        LocalDate curDate = LocalDate.now();
        Semester semester = semesterRepository.findCurrentSemester(curDate);
        if (semester.getDateEnd().equals(curDate) | semester.getDateEnd().isBefore(curDate)) {
            openBSFeature(semester);
            int tmpNum = 1;
            if(semester.getNum() != 3) tmpNum = semester.getNum() +1;
            try {
                semesterRepository.save(new Semester(semester.getDateEnd().plusMonths(3).getYear(),tmpNum,semester.getDateEnd().plusDays(1),semester.getDateEnd().plusMonths(3)));
                return true;
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

//    @Scheduled(cron = "0 0 0 * * ?") // daily
    // Lap lich cho exam plan --> daily
    public void createSchedulingExam(){
        // find examPlan true;
        // take list exam to set....
    }

    //    @Scheduled(cron = "0 0 0 * * ?") // daily
    // Ghi diem chuyen can (attendance score) // daily
    public void updateAttendanceScore(){
        // loc ra cac tkb ngay hom qua so voi ngay hien tai
//        Date yesterday = Date.from(LocalDate.now().minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        LocalDate yesterday = LocalDate.now().minusDays(1);
//        List<ClassCreditsStudents> creditsStudentsAll = new ArrayList<>();
        ComponentPoint componentPoint = componentPointRepository.findByName("attendance");
        List<StudentsScores> studentsScores = new ArrayList<>();
        timeTableRepository.findAllByLessonDate(yesterday)
                .forEach(timeTable -> {
                    ClassCredit classCredit = timeTable.getClassCredit();
                    Subject subject = classCredit.getSubject();
                    ComponentSubject componentSubject = componentSubjectRepository.findByAttendanceScore(componentPoint.getComponentId(),subject.getSubjectId());
                    double period = subject.getCreditNum()*5.0; // tiet hoc = tin chi * 5
                    Double pointForLesson = Math.toIntExact(Math.round(period / 4))*componentSubject.getPercentNumber(); // diem cho 1 buoi hoc
                    List<ClassCreditsStudents> creditsStudents = creditsStudentsRepository.findAllByClassCreditId(classCredit.getClassCreditId());
                    creditsStudents.removeIf(creditsStudent -> timeTable.getStudents().contains(creditsStudent.getStudent()));
                    creditsStudents.forEach(classCreditsStudents -> {
                        StudentsScores studentScore = studentsScoresRepository.findByRegisClass(classCreditsStudents.getClassCredit().getClassCreditId());
                        studentScore.setPointNumber(studentScore.getPointNumber() + pointForLesson);
                        studentsScores.add(studentScore);
                    });
                });
        try {
            studentsScoresRepository.saveAll(studentsScores);
        }catch (Exception e){
            return;
        }
//Stream
//        List<StudentsScores> studentsScores = timeTableRepository.findAllByLessonDate(yesterday).stream()
//                .map(timeTable -> {
//                    ClassCredit classCredit = timeTable.getClassCredit();
//                    ComponentSubject componentSubject = componentSubjectRepository.findByAttendanceScore(componentPoint.getComponentId(), classCredit.getSubject().getSubjectId());
//                    double period = classCredit.getSubject().getCreditNum() * 5.0;
//                    Double pointForLesson = Math.round(period / 4) * componentSubject.getPercentNumber();
//
//                    List<ClassCreditsStudents> creditsStudents = creditsStudentsRepository.findAllByClassCreditId(classCredit.getClassCreditId());
//                    creditsStudents.removeIf(creditsStudent -> timeTable.getStudents().contains(creditsStudent.getStudent()));
//
//                    creditsStudents.forEach(classCreditsStudents -> {
//                        StudentsScores studentScore = studentsScoresRepository.findByRegisClass(classCreditsStudents.getClassCredit().getClassCreditId());
//                        studentScore.setPointNumber(studentScore.getPointNumber() + pointForLesson);
//                        studentsScores.add(studentScore);
//                    });
//
//                    return studentsScores;
//                })
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
    }

    // daily
    //    @Scheduled(cron = "0 0 0 * * ?") // daily
    public void initStudentsScores(){
        List<ClassCredit> classCredits = classCreditRepository.findAllByOpen();
        if (classCredits.isEmpty()) return;
//        ComponentPoint componentPoint = componentPointRepository.findByName("attendance");
//        List<StudentsScores> studentsScores = new ArrayList<>();

        // 2 for long
//        classCredits.forEach(classCredit -> {
//            List<ComponentSubject> componentSubjects = classCredit.getSubject().getComponentSubjects();
//            List<ClassCreditsStudents> creditsStudents = creditsStudentsRepository.findAllByClassCreditId(classCredit.getClassCreditId());
//            componentSubjects.forEach(comSub ->{
//                creditsStudents.forEach(classCreditsStudents -> {
//                    StudentsScores studentScore = new StudentsScores(classCreditsStudents,comSub,0.0);
//                    studentsScores.add(studentScore);
//                });
//            });
//        });


        // chat gpt stream
        List<StudentsScores> studentsScores = classCredits.stream()
                .flatMap(classCredit -> {
                    List<ComponentSubject> componentSubjects = classCredit.getSubject().getComponentSubjects();
                    List<ClassCreditsStudents> creditsStudents = creditsStudentsRepository.findAllByClassCreditId(classCredit.getClassCreditId());
                    if (studentsScoresRepository.findAllByRegisID(creditsStudents.get(0).getId()).isEmpty()) return null;
                    else return componentSubjects.stream()
                            .flatMap(comSub -> creditsStudents.stream()
                                    .map(classCreditsStudents -> new StudentsScores(classCreditsStudents, comSub, 0.0))
                            );
                })
                .collect(Collectors.toList());


        // cach cu chi tao ra diem chuyen can -> bi sai
//        List<StudentsScores> studentsScores = classCredits.stream()
//                .map(classCredit -> {
//                    ComponentSubject componentSubject = componentSubjectRepository.findByAttendanceScore(componentPoint.getComponentId(),classCredit.getSubject().getSubjectId());
//                    List<ClassCreditsStudents> creditsStudents = creditsStudentsRepository.findAllByClassCreditId(classCredit.getClassCreditId());
//                    return creditsStudents.stream()
//                            .map(classCreditsStudents -> new StudentsScores(classCreditsStudents,componentSubject,0.0D))
//                            .collect(Collectors.toList());
//                })
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
        if (!studentsScores.isEmpty()) {
            try {
                studentsScoresRepository.saveAll(studentsScores);
            }catch (Exception e){
                return;
            }
        }
    }
}
