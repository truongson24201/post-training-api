package apisystem.posttraining.DTO.BehaviorPattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BCriteriaSubView {
    @JsonProperty("bCriteriaSubId")
    private Long bCriteriaSubId;
    private String name;
    private String description;
    private Integer maxPoint;
    private boolean status;

    public BCriteriaSubView(Long bCriteriaSubId, String name, String description, boolean status) {
        this.bCriteriaSubId = bCriteriaSubId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public BCriteriaSubView() {
    }

    //    @JsonProperty("bCriteria")
//    private BCriteriaView bCriteria;
}
