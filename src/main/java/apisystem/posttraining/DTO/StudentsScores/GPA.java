package apisystem.posttraining.DTO.StudentsScores;

import lombok.Data;

@Data
public class GPA {
    private Double totalTenPoint;
    private Double totalFourPoint;
    private String letterPoint;
    private boolean result;

    public GPA(Double totalTenPoint, Double totalFourPoint, String letterPoint, boolean result) {
        this.totalTenPoint = totalTenPoint;
        this.totalFourPoint = totalFourPoint;
        this.letterPoint = letterPoint;
        this.result = result;
    }

    public GPA() {
    }
}
