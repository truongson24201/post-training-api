package apisystem.posttraining.DTO.Reward;

import lombok.Data;

@Data
public class RewardPreview {
    private Long facultyId;
    private Long semesterId;
    private Double scoreMin;
    private Integer behaviorMin;
    private Integer amount;
}
