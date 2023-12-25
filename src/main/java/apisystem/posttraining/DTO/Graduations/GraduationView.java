package apisystem.posttraining.DTO.Graduations;

import apisystem.posttraining.DTO.LecturerDTO.LecturerView;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import lombok.Data;

@Data
public class GraduationView {
    private StudentDTO student;
    private LecturerView supervisor;
    private LecturerView thesisAdvisor;
    private Boolean result;
    private String graduationType;
    private String name;
    private String description;

}
