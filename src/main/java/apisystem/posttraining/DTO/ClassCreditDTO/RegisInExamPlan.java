package apisystem.posttraining.DTO.ClassCreditDTO;

import apisystem.posttraining.DTO.LecturerDTO.LecturerView;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisInExamPlan {
    private Long classCreditId;
    private Long subjectId;
    private String subject;
    private Integer numberExamGroups;
    private String examType;
    private Long lecturerId;
    private String lecturerName;
    private Long facultyId;
    private String facultyName;
    private Integer numberStudents;
    private Boolean isCompleted;
}
