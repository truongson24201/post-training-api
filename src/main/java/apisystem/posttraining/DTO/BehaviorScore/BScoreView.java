package apisystem.posttraining.DTO.BehaviorScore;

import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaView;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BScoreView {
    @JsonProperty("bCriteria")
    private BCriteriaView bCriteria;
    @JsonProperty("behaviorScores")
    private List<BehaviorScoreDTO> behaviorScores;
}


