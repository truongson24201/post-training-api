package apisystem.posttraining.DTO.BehaviorPattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BCriteriaView {
    @JsonProperty("bCriteriaId")
    private Long bCriteriaId;
    private String name;
    private boolean status;


}
