package apisystem.posttraining.DTO.StudentsScores;

import lombok.Data;

import java.util.List;

@Data
public class    StudentsScoresAdd {
    private Long regisClassId;
    private List<StudentPointAdd> studentPoints;
}

