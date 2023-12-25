package apisystem.posttraining.DTO.StudentsScores;

import apisystem.posttraining.DTO.ComSubjectDTO.ComSubjectView;
import apisystem.posttraining.entity.ComponentSubject;
import lombok.Data;

@Data
public class ComPointView {
    private ComSubjectView componentSubject;
    private Double pointNumber;
}
