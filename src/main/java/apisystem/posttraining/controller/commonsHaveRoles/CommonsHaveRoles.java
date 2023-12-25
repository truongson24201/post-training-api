package apisystem.posttraining.controller.commonsHaveRoles;

import apisystem.posttraining.service.ICommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/commons-roles")
@RequiredArgsConstructor
public class CommonsHaveRoles {
    private final ICommonService commonService;

    @GetMapping("feature-sheet")
    public ResponseEntity<?> checkTimeUpdateSheet(@RequestParam("id") Long semesterId){
        return commonService.checkTimeUpdateSheet(semesterId);
    }

    @GetMapping("classes")
    public ResponseEntity<?> getAllClass(){
        return commonService.getAllClass();
    }

    @PreAuthorize("hasAuthority('TQA')")
    @GetMapping("classes-in-exam")
    public ResponseEntity<?> getAllClassInExam(){
        return commonService.getAllClassInExam();
    }

    @GetMapping("total-student")
    public Integer getTotalStudentInClass(@RequestParam("id") Long classId){
        return commonService.getTotalStudentInClass(classId);
    }

    @GetMapping("students")
    public ResponseEntity<?> getAllStudents(){
        return commonService.getAllStudents();
    }
}
