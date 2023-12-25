package apisystem.posttraining.service;

import apisystem.posttraining.DTO.BehaviorScore.BScoreAdd;
import apisystem.posttraining.DTO.BehaviorScore.BehaviorScoreDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBehaviorScoreService {
    ResponseEntity<?> getAllBehaviorScore(Long classId, Long semesterId, int index);

    ResponseEntity<?> addBehaviorScore(Long bSheetId, List<BScoreAdd> BScoreAdd);


    ResponseEntity<?> checkPatternIsBlock(Long bSPatternId);

    ResponseEntity<?> viewBSheet(Long semesterId,String username);
}
