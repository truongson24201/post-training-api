package apisystem.posttraining.DTO.BehaviorSheetDTO;

import apisystem.posttraining.DTO.SemesterDTO.SemesterDTO;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import lombok.Data;

@Data
public class BehaviorSheetDTO {
    private Long bSheetId;
    private SemesterDTO semester;
    private StudentDTO student;
}
