package apisystem.posttraining.DTO.BehaviorPattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
    public class BSContentAdd {
        @JsonProperty("bCriteriaSubId")
        private Long bCriteriaSubId;
        private Integer maxPoint;
}
