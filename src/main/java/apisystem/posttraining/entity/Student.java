package apisystem.posttraining.entity;


import apisystem.posttraining.entity.enumreration.EStudentStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "student",uniqueConstraints = {
        @UniqueConstraint(columnNames = "student_id")
})
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "studentId")
public class Student  implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private String studentId;

//    @Column(name = "academic_year")
//    private String academicYear;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id")
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id",referencedColumnName = "account_id")
    private Account account;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "student")
    private List<BehaviorSheet> bSheets;

    @Column(name = "academic_year")
    private String academicYear;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "student")
    private List<ClassCreditsStudents> classCredits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id",referencedColumnName = "class_id")
    private Class aClass;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "students")
    private List<Exam> exams;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EStudentStatus status;

}
