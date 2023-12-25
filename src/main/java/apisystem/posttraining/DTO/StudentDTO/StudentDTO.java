package apisystem.posttraining.DTO.StudentDTO;

import apisystem.posttraining.DTO.ClassDTO.ClassDTO;
import apisystem.posttraining.DTO.ProfileDTO.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudentDTO {
    private String studentId;
    @JsonProperty("aClass")
    private ClassDTO aClass;
    private ProfileDTO profile;

}
