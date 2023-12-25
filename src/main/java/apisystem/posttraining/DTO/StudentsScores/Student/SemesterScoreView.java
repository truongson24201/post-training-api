package apisystem.posttraining.DTO.StudentsScores.Student;

import apisystem.posttraining.DTO.SemesterDTO.SemesterDTO;
import lombok.Data;

import java.util.List;

@Data
public class SemesterScoreView {
    private SemesterDTO semester;
    private List<SubjectPointView> subjectPoint;
    private Double semesterAverageFour;
    private Double semesterAverageTen;
    private Integer semesterCreditNum;
    private Double averageFour;
    private Double averageTen;
    private Integer creditNum;

    public SemesterScoreView(SemesterDTO semester, List<SubjectPointView> subjectPoint, Double semesterAverageFour, Double semesterAverageTen, Integer semesterCreditNum, Double averageFour, Double averageTen, Integer creditNum) {
        this.semester = semester;
        this.subjectPoint = subjectPoint;
        this.semesterAverageFour = semesterAverageFour;
        this.semesterAverageTen = semesterAverageTen;
        this.semesterCreditNum = semesterCreditNum;
        this.averageFour = averageFour;
        this.averageTen = averageTen;
        this.creditNum = creditNum;
    }

    public SemesterScoreView(SemesterDTO semester, List<SubjectPointView> subjectPoint, Double semesterAverageFour, Double semesterAverageTen, Integer semesterCreditNum) {
        this.semester = semester;
        this.subjectPoint = subjectPoint;
        this.semesterAverageFour = semesterAverageFour;
        this.semesterAverageTen = semesterAverageTen;
        this.semesterCreditNum = semesterCreditNum;
    }

    public SemesterScoreView() {
    }
}
