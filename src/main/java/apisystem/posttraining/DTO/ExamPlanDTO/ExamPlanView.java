package apisystem.posttraining.DTO.ExamPlanDTO;

import apisystem.posttraining.DTO.ClassCreditDTO.ClassCreditView;
import apisystem.posttraining.DTO.RegisExamDTO.RegisExamPlanView;
import apisystem.posttraining.DTO.SemesterDTO.SemesterDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class ExamPlanView {
    private Long examPlanId;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateStart;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateEnd;
    private String title;
    private Boolean isDelete;
    private SemesterDTO semester;
    private Boolean flag;
    private Boolean status;
//    private List<RegisExamPlanView> regisExamPlans;
//    private List<ClassCreditView> classCredits;
}
