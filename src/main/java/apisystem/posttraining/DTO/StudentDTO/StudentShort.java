package apisystem.posttraining.DTO.StudentDTO;

import apisystem.posttraining.DTO.ProfileDTO.ProfileDTO;
import lombok.Data;

@Data
public class StudentShort {
    private String studentId;
    private ProfileDTO profile;
}
