package apisystem.posttraining.DTO.Reward;

import lombok.Data;

@Data
public class RewardAdd {
    private String studentId;
    private Double gpaFound;
    private Integer gpaBehavior;
    private String content;
}
