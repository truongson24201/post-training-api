package apisystem.posttraining.service;

import apisystem.posttraining.DTO.Reward.RewardAdd;
import apisystem.posttraining.DTO.Reward.RewardPreview;
import apisystem.posttraining.DTO.Reward.UpdateRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRewardService {
    ResponseEntity<?> previewRewardsStudents(RewardPreview request);

    ResponseEntity<?> getRewardsStudents(Long semesterId);

    ResponseEntity<?> addRewardsStudents(Long semesterId, List<RewardAdd> request);

    ResponseEntity<?> updateRewardsStudents(Long facultyId, Long semesterId, List<RewardAdd> request);

    boolean checkHasReward(Long facultyId, Long semesterId);
}
