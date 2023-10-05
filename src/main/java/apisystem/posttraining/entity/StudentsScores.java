package apisystem.posttraining.entity;


import apisystem.posttraining.entity.id.StudentScoreID;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "students_scores")
@Data
@IdClass(StudentScoreID.class)
public class StudentsScores {
    @Id
    @ManyToOne
    @JoinColumn(name = "regis_class_id")
    private ClassCreditsStudents regisClass;

    @Id
    @ManyToOne
    @JoinColumn(name = "component_subject_id")
    private ComponentSubject componentSubject;

    @Column(name = "point_number")
    private Double pointNumber;
}
