package apisystem.posttraining.DTO.Reward;

import lombok.Data;

@Data
public class GpaReward {
    private Double totalTenPoint;
    private Double totalFourPoint;
    private Integer totalBehaviorPoint;
    private String content;

    public GpaReward(Double totalTenPoint, Double totalFourPoint, Integer totalBehaviorPoint) {
        this.totalTenPoint = totalTenPoint;
        this.totalFourPoint = totalFourPoint;
        this.totalBehaviorPoint = totalBehaviorPoint;
    }

    public GpaReward(Double totalFourPoint, Integer totalBehaviorPoint, String content) {
        this.totalFourPoint = totalFourPoint;
        this.totalBehaviorPoint = totalBehaviorPoint;
        this.content = content;
    }

    public GpaReward() {
    }
}
