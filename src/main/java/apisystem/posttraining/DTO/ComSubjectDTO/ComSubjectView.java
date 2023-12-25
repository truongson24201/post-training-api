package apisystem.posttraining.DTO.ComSubjectDTO;

import lombok.Data;

@Data
public class ComSubjectView {
    private Long comSubId;
    private ComPointDTO component;
    private Double percentNumber;
}

