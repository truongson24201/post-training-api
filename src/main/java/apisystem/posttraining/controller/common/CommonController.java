package apisystem.posttraining.controller.common;

import apisystem.posttraining.service.ICommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/commons")
@RequiredArgsConstructor
public class CommonController {
    private final ICommonService commonService;

    @GetMapping("semesters")
    public ResponseEntity<?> getAllSemester(){
        return commonService.getAllSemester();
    }

    @GetMapping("semesters/current")
    public ResponseEntity<?> getCurrentSemester(){
        return commonService.getCurrentSemester();
    }

    @GetMapping("faculties")
    public ResponseEntity<?> getAllFaculties(){
        return commonService.getAllFaculties();
    }

    @GetMapping("shift-systems")
    public ResponseEntity<?> getShiftSystem(){
        return commonService.getAllShiftSystem();
    }

    @GetMapping("{id}/lecturers")
    public ResponseEntity<?> getAllLecturer(@PathVariable("id") Long examPlanId){
        return commonService.getAllLecturer(examPlanId);
    }

    @GetMapping("students")
    public ResponseEntity<?> getAllStudentsInClass(@RequestParam(name = "classId") Long classId){
        return commonService.getAllStudentsInClass(classId);
    }

    @GetMapping("expired-semester")
    public boolean checkExpiredToEnterPoint(@RequestParam(name = "semesterId") Long semesterId){
        return commonService.checkExpiredToEnterPoint(semesterId);
    }

    @GetMapping("semester-permission")
    public ResponseEntity<?> getSemesterToPermission(){
        return commonService.getSemesterToPermission();
    }

    @GetMapping("free-lec-in-exam")
    public ResponseEntity<?> getFreeLecInExam(@RequestParam(name = "examId") Long examId){
        return commonService.getFreeLecInExam(examId);
    }

}
