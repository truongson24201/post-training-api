package apisystem.posttraining.DTO.ClassDTO;

import apisystem.posttraining.DTO.FacultyDTO.FacultyDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClassDTO {
    private Long classId;
    @JsonProperty("className")
    private String name;
    private FacultyDTO faculty;
}
