package apisystem.posttraining.service;

import apisystem.posttraining.DTO.ExamDTO.ChangeLecRequest;
import org.springframework.http.ResponseEntity;

public interface IExamService {

    ResponseEntity<?> buildExamSchedulingForExamPlan(Long examPlanId);

    ResponseEntity<?> getExamScheduling(Long examPlanId);

    ResponseEntity<?> getExamDetails(Long examId);

    ResponseEntity<?> getStudentsInExam(Long examId);

    ResponseEntity<?> getProctoringOfLecurrer();

    ResponseEntity<?> getAllExamToStudent(Long examPlanId,String studentId);

    ResponseEntity<?> changeLec(ChangeLecRequest request);
}
