package apisystem.posttraining.DTO.ExamDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ShiftSystemView {
    @JsonProperty("shiftSystemId")
    private Integer shiftSystemId;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeStart;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeClose;
    private Boolean type;
}
