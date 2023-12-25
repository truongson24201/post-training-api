package apisystem.posttraining.DTO.Reward;

import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import lombok.Data;

@Data
public class RewardsView {
    private StudentDTO student;
    private GpaReward gpaReward;

    public RewardsView(StudentDTO student, GpaReward gpaReward) {
        this.student = student;
        this.gpaReward = gpaReward;
    }

    public RewardsView() {
    }
}
