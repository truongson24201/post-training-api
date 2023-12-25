package apisystem.posttraining.service;

import apisystem.posttraining.DTO.ExamPlanDTO.ExamPlanAdd;
import apisystem.posttraining.DTO.RegisExamDTO.RegisExamPlanAdd;
import org.springframework.http.ResponseEntity;

public interface IExamPlanService {
    ResponseEntity<?> addExamPlan(ExamPlanAdd examPlanAdd);

    ResponseEntity<?> getAllExamPlan();

    ResponseEntity<?> getDetailsExamPlan(Long examPlanId);

    ResponseEntity<?> regisIntoExamPlan(RegisExamPlanAdd regisExamPlanAdd);

    ResponseEntity<?> updateExamPlan(Long examPlanId, ExamPlanAdd examPlanAdd);

    ResponseEntity<?> deleteExamPlan(Long examPlanId);

    ResponseEntity<?> getRegisInExamPlan(Long examPlanId);

    ResponseEntity<?> updateCCInExamPlan(RegisExamPlanAdd classCredit);

    ResponseEntity<?> deleteCCInExamPlan(Long classCreditId);

    ResponseEntity<?> updateFlag(Long examPlanId);

    ResponseEntity<?> getAllExamPlanHasBuild();

    ResponseEntity<?> closeAllHasExpired();
}
