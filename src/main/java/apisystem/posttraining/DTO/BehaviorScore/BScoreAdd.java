package apisystem.posttraining.DTO.BehaviorScore;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BScoreAdd {
    @JsonProperty("bCriteriaSubId")
    private Long bCriteriaSubId;
    private Integer selfPoint;
    private Integer classPoint;
    private Integer advisorPoint;
}
