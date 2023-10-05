package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "classcredits_students")
@Data
public class ClassCreditsStudents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_credit_id")
    private ClassCredit classCredit;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "status")
    private Boolean status;

}
