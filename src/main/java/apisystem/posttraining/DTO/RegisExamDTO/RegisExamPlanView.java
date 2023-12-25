package apisystem.posttraining.DTO.RegisExamDTO;

import apisystem.posttraining.DTO.ClassCreditDTO.ClassCreditView;
import apisystem.posttraining.DTO.ExamPlanDTO.ExamPlanView;
import apisystem.posttraining.entity.ClassCredit;
import apisystem.posttraining.entity.enumreration.EExamType;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class RegisExamPlanView {
//    private Long regis_exam_plan_id;
    private ClassCreditView classCredit;
//    private ExamPlanView examPlan;
    private Integer numberGroup;
    private String examType;
}
