package apisystem.posttraining.DTO.ExamDTO;

import lombok.Data;

@Data
public class ChangeLecRequest {
    private Long examId;
    private Long lecOldId;
    private Long lecNewId;
}
