package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "class_credit")
@Data
public class ClassCredit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_credit_id")
    private Long classCreditId;

    @Column(name = "group_number")
    private Integer groupNumber;

    @Column(name = "min_size")
    private Integer minSize;

    @Column(name = "max_size")
    private Integer maxSize;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "regis_opening")
    private Date regisOpening;

    @Column(name = "regis_closing")
    private Date regisClosing;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
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
    private List<Exam> examList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "classcredits_students",
            joinColumns = @JoinColumn(name = "class_credit_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

}
