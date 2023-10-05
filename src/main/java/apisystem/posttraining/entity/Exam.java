package apisystem.posttraining.entity;

import apisystem.posttraining.entity.enumreration.EExamType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "exam")
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "exam_date")
    private Date examDate;

    @Column(name = "exam_time")
    private Date examTime;

    @Column(name = "group_number")
    private Integer groupNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    private EExamType examType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_plan_id",referencedColumnName = "exam_plan_id")
    private ExamPlan examPlan;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_credit_id",referencedColumnName = "class_credit_id")
    private ClassCredit classCredit;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "classroom_id",referencedColumnName = "classroom_id")
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "shift_system_id",referencedColumnName = "shift_system_id")
    private ShiftSystem shiftSystem;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "exams_lecturers",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id"))
    private List<Lecturer> lecturers;


}
