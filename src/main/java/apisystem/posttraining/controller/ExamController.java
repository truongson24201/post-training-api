package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.ExamDTO.ChangeLecRequest;
import apisystem.posttraining.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/exam-scheduling")
public class ExamController {
    private final IExamService examService;


    @GetMapping("")
    public ResponseEntity<?> getExamScheduling(@RequestParam(name = "id") Long examPlanId){
        return examService.getExamScheduling(examPlanId);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getExamDetails(@PathVariable(name = "id") Long examId){
        return examService.getExamDetails(examId);
    }

    @GetMapping("{id}/students")
    public ResponseEntity<?> getStudentsInExam(@PathVariable(name = "id") Long examId){
        return examService.getStudentsInExam(examId);
    }

    @PreAuthorize("hasAuthority('lecturer')")
    @GetMapping("proctoring")
    public ResponseEntity<?> getProctoringOfLecturer(){
        return examService.getProctoringOfLecurrer();
    }

    @GetMapping("student")
    public ResponseEntity<?> getAllExamToStudent(@RequestParam(name = "examPlanId") Long examPlanId,
                                                 @RequestParam(name = "studentId") String studentId){
        return examService.getAllExamToStudent(examPlanId,studentId);
    }

    @PutMapping("change-lec")
    public ResponseEntity<?> changeLec(@RequestBody ChangeLecRequest request){
        return examService.changeLec(request);
    }



}
