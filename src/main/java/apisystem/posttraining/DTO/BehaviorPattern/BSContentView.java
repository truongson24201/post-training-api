package apisystem.posttraining.DTO.BehaviorPattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BSContentView {
    @JsonProperty("bCriteriaId")
    private Long bCriteriaId;
    private String name;
    @JsonProperty("bCriteriaSubs")
    private List<BCriteriaSubView> bCriteriaSubs;
}
