package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "exam",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"class_credit_id", "group_number"})})
@Getter
@Setter
public class Exam  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "exam_date",columnDefinition = "DATE")
    private LocalDate examDate;

//    @Column(name = "exam_time",columnDefinition = "TIME")
//    private LocalTime examTime;

    @Column(name = "group_number")
    private Integer groupNumber;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "exam_type")
//    private EExamType examType;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "class_credit_id",referencedColumnName = "class_credit_id")
//    private RegisExamPlan regisExamPlan;
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

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "students_exams",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

//    public Exam(Long examId, LocalDate examDate, Integer groupNumber, RegisExamPlan regisExamPlan, Classroom classroom, ShiftSystem shiftSystem) {
//        this.examId = examId;
//        this.examDate = examDate;
////        this.examTime = examTime;
//        this.groupNumber = groupNumber;
//        this.regisExamPlan = regisExamPlan;
//        this.classroom = classroom;
//        this.shiftSystem = shiftSystem;
//    }


    public Exam(LocalDate examDate, Integer groupNumber, ClassCredit classCredit, Classroom classroom, ShiftSystem shiftSystem, List<Student> students) {
        this.examDate = examDate;
        this.groupNumber = groupNumber;
        this.classCredit = classCredit;
        this.classroom = classroom;
        this.shiftSystem = shiftSystem;
        this.students = students;

    }

    public Exam() {
    }
}
