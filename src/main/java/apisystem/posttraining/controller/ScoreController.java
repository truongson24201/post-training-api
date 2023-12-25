package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.StudentsScores.StudentsScoresAdd;
import apisystem.posttraining.DTO.StudentsScores.UpExamStatus;
import apisystem.posttraining.service.IScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final IScoreService scoreService;


    @PutMapping("")
    public ResponseEntity<?> updateStudentsScores(@RequestBody List<StudentsScoresAdd> studentsScoresAdd){
        return scoreService.updateStudentsScores(studentsScoresAdd);
    }

    //giang vien va phong khao thi
    @GetMapping("")
    public ResponseEntity<?> getStudentsScoresWithClass(@RequestParam(name = "classCreditId",required = false) Long classCreditId){
//        if (classCreditId != null)
            return scoreService.getStudentsScores(classCreditId);
//        else return scoreService.getStudentScores();
    }

    @GetMapping("exam")
    public ResponseEntity<?> getStudentsScoresWithExam(@RequestParam(name = "id",required = false) Long examId){
//        if (classCreditId != null)
        return scoreService.getStudentsScoresWithExam(examId);
//        else return scoreService.getStudentScores();
    }

    @GetMapping("student")
    public ResponseEntity<?> getScores(@RequestParam(name = "id",required = false) String studentId){
        return scoreService.getStudentScores(studentId);
    }

    @PutMapping("exam-status")
    public ResponseEntity<?> saveStudentsExamStatus(@RequestBody List<UpExamStatus> upExamStatus){
        return scoreService.saveStudentsExamStatus(upExamStatus);
    }


    @PutMapping("{id}/attendance")
    public ResponseEntity<?> updateAttendanceScores(@PathVariable("id") Long classCreditId){
        return scoreService.updateAttendanceScores(classCreditId);
    }

    @GetMapping("expired-enter")
    public boolean checkExpiredToEnterPoint(@RequestParam(name = "semesterId") Long semesterId){
        return scoreService.checkExpiredToEnterPoint(semesterId);
    }


}
