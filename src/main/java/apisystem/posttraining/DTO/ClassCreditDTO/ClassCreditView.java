package apisystem.posttraining.DTO.ClassCreditDTO;

import apisystem.posttraining.DTO.LecturerDTO.LecturerView;
import apisystem.posttraining.DTO.SemesterDTO.SemesterDTO;
import apisystem.posttraining.DTO.SubjectDTO.SubjectView;
import apisystem.posttraining.entity.Semester;
import apisystem.posttraining.entity.Subject;
import apisystem.posttraining.entity.enumreration.EClassCredit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class ClassCreditView {
    private Long classCreditId;
    private String name;
    private Integer groupNumber;
    private SubjectView subject;
    private SemesterDTO semester;
    private Integer numberExamGroups;
    private String examType;
    private LecturerView lecturer;
}
