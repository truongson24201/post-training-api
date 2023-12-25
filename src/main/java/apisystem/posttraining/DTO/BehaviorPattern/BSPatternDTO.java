package apisystem.posttraining.DTO.BehaviorPattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BSPatternDTO {
    private Integer ordinal;
    @JsonProperty("bSPatternId")
    private Long bSPatternId;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateClose;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
//    @JsonFormat(pattern = "dd/MM/yyyy-HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateOpen;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate updateOn;
    private String updateBy;
    private Boolean status;



}
