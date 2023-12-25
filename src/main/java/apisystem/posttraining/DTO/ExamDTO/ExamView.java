package apisystem.posttraining.DTO.ExamDTO;

import apisystem.posttraining.DTO.ClassCreditDTO.ClassCreditView;
import apisystem.posttraining.DTO.ClassCreditDTO.RegisInExamPlan;
import apisystem.posttraining.DTO.ExamPlanDTO.ExamPlanView;
import apisystem.posttraining.DTO.LecturerDTO.LecturerView;
import apisystem.posttraining.DTO.StudentDTO.StudentDTO;
import apisystem.posttraining.entity.*;
import apisystem.posttraining.entity.enumreration.EExamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class ExamView {
    private Long examId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate examDate;
    private Integer groupNumber;
//    private ExamPlanView examPlan;
    private RegisInExamPlan classCredit;
    private Long classroomId;
    private String classroomName;
    private ShiftSystemView shiftSystem;
    private List<StudentDTO> students;
    private Integer studentSize;
    private List<LecturerView> lecturers;
//    private ClassCredit classCredit;
//    private Classroom classroom;
//    private ShiftSystem shiftSystem;
//    private List<Lecturer> lecturers;
}
