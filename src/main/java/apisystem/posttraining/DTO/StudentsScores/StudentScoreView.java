package apisystem.posttraining.DTO.StudentsScores;

import apisystem.posttraining.DTO.StudentRegisDTO.StudentRegisView;
import lombok.Data;

import java.util.List;

@Data
public class StudentScoreView {
    private StudentRegisView regisClass;
    private List<ComPointView> studentPoint;

    public StudentScoreView(StudentRegisView regisClass, List<ComPointView> studentPoint) {
        this.regisClass = regisClass;
        this.studentPoint = studentPoint;
    }

    public StudentScoreView() {
    }
}
