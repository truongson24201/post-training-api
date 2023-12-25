package apisystem.posttraining.DTO.BehaviorPattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BCriteriaSubAdd {
    @JsonProperty("bCriteriaId")
    private Long bCriteriaId;
    private String name;
    private String description;
}
