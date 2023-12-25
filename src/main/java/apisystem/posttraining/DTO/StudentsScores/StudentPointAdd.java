package apisystem.posttraining.DTO.StudentsScores;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudentPointAdd {
    @JsonProperty("comSubId")
    private Long comSubId;
    private Double pointNumber;
}
