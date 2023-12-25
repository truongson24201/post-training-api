package apisystem.posttraining.entity;


import apisystem.posttraining.entity.id.ClassCreditStudentID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "classcredits_students",
    uniqueConstraints = @UniqueConstraint(columnNames = {"class_credit_id","student_id"})
)
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ClassCreditsStudents  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "class_credit_id")
    private ClassCredit classCredit;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "exam_status")
    private Boolean examStatus;

    public ClassCreditsStudents(ClassCredit classCredit, Student student, Boolean status, Boolean examStatus) {
        this.classCredit = classCredit;
        this.student = student;
        this.status = status;
        this.examStatus = examStatus;
    }

    public ClassCreditsStudents() {
    }
}
