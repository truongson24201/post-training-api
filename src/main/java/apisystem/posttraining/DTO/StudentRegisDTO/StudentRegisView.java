package apisystem.posttraining.DTO.StudentRegisDTO;

import apisystem.posttraining.DTO.ProfileDTO.ProfileDTO;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import lombok.Data;

@Data
public class StudentRegisView {
    private Long id;
    private StudentDTO student;
    private Boolean examStatus;


}

record StudentProfile(Long studentId, ProfileDTO profile){}
