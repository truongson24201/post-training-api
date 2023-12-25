package apisystem.posttraining.DTO.Graduations;

import lombok.Data;

@Data
public class GraduationGPA {
    private Double gpaFound;
    private Integer creditNum;
    private Integer pointDNum;
    private Boolean result;

    public GraduationGPA(Double gpaFound, Integer creditNum, Integer pointDNum) {
        this.gpaFound = gpaFound;
        this.creditNum = creditNum;
        this.pointDNum = pointDNum;
    }

    public GraduationGPA(Double gpaFound, Integer creditNum, Boolean result) {
        this.gpaFound = gpaFound;
        this.creditNum = creditNum;
        this.result = result;
    }

    public GraduationGPA(Double gpaFound, Integer creditNum, Integer pointDNum, Boolean result) {
        this.gpaFound = gpaFound;
        this.creditNum = creditNum;
        this.pointDNum = pointDNum;
        this.result = result;
    }

    public GraduationGPA() {
    }

    public GraduationGPA(Integer creditNum) {
        this.creditNum = creditNum;
    }
}
