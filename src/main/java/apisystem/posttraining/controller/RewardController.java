package apisystem.posttraining.controller;

import apisystem.posttraining.DTO.Reward.RewardAdd;
import apisystem.posttraining.DTO.Reward.RewardPreview;
import apisystem.posttraining.DTO.Reward.UpdateRequest;
import apisystem.posttraining.service.IRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final IRewardService rewardService;

    @PostMapping("preview")
    public ResponseEntity<?> previewRewardsStudents(@RequestBody RewardPreview request){
        return rewardService.previewRewardsStudents(request);
    }

    @GetMapping("check")
    public boolean checkHasReward(@RequestParam(name = "facultyId") Long facultyId,
                                  @RequestParam(name = "semesterId") Long semesterId){
        return rewardService.checkHasReward(facultyId,semesterId);
    }

    @PostMapping("{semester}")
    public ResponseEntity<?> addRewardsStudents(@PathVariable("semester") Long semesterId,
                                                @RequestBody List<RewardAdd> request){
        return rewardService.addRewardsStudents(semesterId,request);
    }

    @PutMapping("{faculty}/{semester}")
    public ResponseEntity<?> updateRewardsStudents(@PathVariable("faculty") Long facultyId,
                                                   @PathVariable("semester") Long semesterId,
                                                   @RequestBody List<RewardAdd> request){
        return rewardService.updateRewardsStudents(facultyId,semesterId,request);
    }

    @GetMapping("")
    public ResponseEntity<?> getRewardsStudents(@RequestParam(name = "semesterId") Long semesterId){
        return rewardService.getRewardsStudents(semesterId);
    }
}
