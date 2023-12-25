package apisystem.posttraining.DTO.Graduations;

import lombok.Data;

@Data
public class GraThesisPreviewRequest {
    private Long facultyId;
    private String makeYear;
    private Double gpaFoundMin;
    private Integer creditMin;
    private Integer pointDMax;
    private Integer amount;
}
