package apisystem.posttraining.service.Impl;

import apisystem.posttraining.DTO.StudentsScores.ComPointView;
import apisystem.posttraining.DTO.StudentsScores.GPA;
import apisystem.posttraining.DTO.StudentsScores.Student.SubjectPointView;
import apisystem.posttraining.DTO.SubjectDTO.SubjectView;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.repository.*;
import apisystem.posttraining.service.IContextHolder;
import apisystem.posttraining.utils.CommonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl {
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
    private final CreditsStudentsRepository creditsStudentsRepository;
    private final StudentsScoresRepository studentsScoresRepository;



    public boolean initBehavior(Long semesterId, Long bsId) {
        Semester semester = semesterRepository.findById(semesterId).get();
        BSPattern bsPattern = bsPatternRepository.findById(bsId).get();
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
        return true;
    }

    public boolean updateStatus() {
        List<ClassCreditsStudents> creditsStudentss = creditsStudentsRepository.findAll();
        for (ClassCreditsStudents s : creditsStudentss) {
            List<StudentsScores> studentsScores = studentsScoresRepository.findAllByRegisID(s.getId());
            GPA gpa = new GPA(0.0, 0.0, "", false);

            gpa.setTotalTenPoint(CommonService.calTenPointOneSubject1(studentsScores));
            CommonService.convertTenToFour(gpa);
            boolean hasZeroPoint = studentsScores.stream()
                    .anyMatch(score -> score.getPointNumber() == 0);
            if (hasZeroPoint || !gpa.isResult())
                s.setStatus(false);
            else  s.setStatus(true);
        }
        try{
            creditsStudentsRepository.saveAll(creditsStudentss);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
