package apisystem.posttraining.DTO.LecturerDTO;

import apisystem.posttraining.DTO.FacultyDTO.FacultyDTO;
import apisystem.posttraining.DTO.ProfileDTO.ProfileDTO;
import apisystem.posttraining.entity.Faculty;
import lombok.Data;

@Data
public class LecturerView {
    private Long lecturerId;
    private ProfileDTO profile;
    private FacultyDTO faculty;

}
