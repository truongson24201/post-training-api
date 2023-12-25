package apisystem.posttraining.DTO.RegisExamDTO;

import apisystem.posttraining.entity.enumreration.EExamType;
import lombok.Data;

@Data
public class RegisExamPlanAdd {
    private Long examPlanId;
    private Long classCreditId;
    private Integer numberExamGroup;
    private String examType;

}
