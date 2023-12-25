package apisystem.posttraining.entity;


import apisystem.posttraining.entity.enumreration.EClassCredit;
import apisystem.posttraining.entity.enumreration.EExamType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "class_credit",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"semester_id","subject_id","faculty_id"})}
)
@Getter
@Setter
public class ClassCredit implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_credit_id")
    private Long classCreditId;

    @Column(name = "group_number")
    private Integer groupNumber;

    @Nationalized
    @Column(name = "name")
    private String name;

    @Column(name = "min_size")
    private Integer minSize;

    @Column(name = "max_size")
    private Integer maxSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EClassCredit status;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "regis_opening")
    private Date regisOpening;

    @Column(name = "regis_closing")
    private Date regisClosing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id",referencedColumnName = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "lecturer_id",referencedColumnName = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "faculty_id",referencedColumnName = "faculty_id")
    private Faculty faculty;

//    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
//    @JoinColumn(name = "class_id",referencedColumnName = "class_id")
//    private Class aClass;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "classCredit")
    private List<TimeTable> timeTables;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "exam_plan_id",referencedColumnName = "exam_plan_id")
    private ExamPlan examPlan;

    @Column(name = "number_exam_groups")
    private Integer numberExamGroups;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    private EExamType examType;

    // Con nghi van vi dang ra phai OneToMany voi ClassCreditStudents
//    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "classcredits_students",
//            joinColumns = @JoinColumn(name = "class_credit_id"),
//            inverseJoinColumns = @JoinColumn(name = "student_id")
//    )
//    private List<Student> students;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "classCredit")
    private List<ClassCreditsStudents> students;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "classCredit")
    private List<Exam> exams;

}
