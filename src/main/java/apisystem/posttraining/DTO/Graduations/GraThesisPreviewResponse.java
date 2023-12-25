package apisystem.posttraining.DTO.Graduations;

import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import lombok.Data;

@Data
public class GraThesisPreviewResponse {
    private StudentDTO student;
    private GraduationGPA graduationGPA;

    public GraThesisPreviewResponse(StudentDTO student, GraduationGPA graduationGPA) {
        this.student = student;
        this.graduationGPA = graduationGPA;
    }

    public GraThesisPreviewResponse() {
    }
}
