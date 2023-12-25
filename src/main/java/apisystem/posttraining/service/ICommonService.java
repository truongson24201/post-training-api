package apisystem.posttraining.service;

import apisystem.posttraining.DTO.Account.RequestLogin;
import apisystem.posttraining.controller.AuthController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

public interface ICommonService {
    ResponseEntity<?> getAllSemester();

    ResponseEntity<?> checkTimeUpdateSheet(Long semesterId);

    ResponseEntity<?> getAllClass();

    Integer getTotalStudentInClass(Long classId);

    ResponseEntity<?> login(RequestLogin requestLogin);

    ResponseEntity<?> getCurrentSemester();

    ResponseEntity<?> getAllFaculties();

    ResponseEntity<?> getAllShiftSystem();

    ResponseEntity<?> getAllLecturer(Long examPlanId);

    ResponseEntity<?> getAllStudents();

    ResponseEntity<?> getAllClassInExam();

    ResponseEntity<?> getAllStudentsInClass(Long classId);

    boolean checkExpiredToEnterPoint(Long semesterId);

    ResponseEntity<?> getSemesterToPermission();

    ResponseEntity<?> getFreeLecInExam(Long examId);
}
