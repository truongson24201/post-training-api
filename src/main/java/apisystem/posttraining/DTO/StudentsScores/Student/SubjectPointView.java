package apisystem.posttraining.DTO.StudentsScores.Student;

import apisystem.posttraining.DTO.ClassCreditDTO.ClassCreditView;
import apisystem.posttraining.DTO.StudentsScores.ComPointView;
import apisystem.posttraining.DTO.StudentsScores.GPA;
import apisystem.posttraining.DTO.SubjectDTO.SubjectView;
import lombok.Data;

import java.util.List;

@Data
public class SubjectPointView {
    private SubjectView subject;
    private List<ComPointView> studentPoint;
    private GPA gpa;
    private Long classCreditId;

    public SubjectPointView(SubjectView subject, List<ComPointView> studentPoint, GPA gpa, Long classCreditId) {
        this.subject = subject;
        this.studentPoint = studentPoint;
        this.gpa = gpa;
        this.classCreditId = classCreditId;
    }

    //    public SubjectPointView(ClassCreditView classCredit, List<ComPointView> studentPoint, GPA gpa) {
//        this.classCredit = classCredit;
//        this.studentPoint = studentPoint;
//        this.gpa = gpa;
//    }

    public SubjectPointView() {
    }
}
