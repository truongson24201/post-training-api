package apisystem.posttraining.DTO.SubjectDTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SubjectView {
    private Long subjectId;
    private String name;
    private Integer creditNum;
    private Integer theoryNum;
    private Integer practicalNum;
    private String academicYear;
}
