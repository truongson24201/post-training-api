package apisystem.posttraining.DTO.ExamPlanDTO;

import apisystem.posttraining.DTO.SemesterDTO.SemesterDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ExamPlanAdd {
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateStart;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateEnd;
    private String title;
}
