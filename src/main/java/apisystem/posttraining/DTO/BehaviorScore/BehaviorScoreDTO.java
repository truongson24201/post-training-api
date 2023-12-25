package apisystem.posttraining.DTO.BehaviorScore;

import apisystem.posttraining.DTO.BehaviorPattern.BCriteriaSubView;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BehaviorScoreDTO {
    @JsonProperty("bCriteriaSub")
    private BCriteriaSubView bCriteriaSub;
    private Integer selfPoint;
    private Integer classPoint;
    private Integer advisorPoint;
}
