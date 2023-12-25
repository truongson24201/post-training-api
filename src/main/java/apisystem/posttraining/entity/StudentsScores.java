package apisystem.posttraining.entity;


import apisystem.posttraining.entity.id.StudentScoreID;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name = "students_scores")
@Getter
@Setter
@IdClass(StudentScoreID.class)
public class StudentsScores  implements Serializable {
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

    public StudentsScores(ClassCreditsStudents regisClass, ComponentSubject componentSubject, Double pointNumber) {
        this.regisClass = regisClass;
        this.componentSubject = componentSubject;
        this.pointNumber = pointNumber;
    }

    public StudentsScores(ClassCreditsStudents regisClass, ComponentSubject componentSubject) {
        this.regisClass = regisClass;
        this.componentSubject = componentSubject;
    }

    @PrePersist
    private void setDefaultPoint(){
        if (this.componentSubject.getComponent().getComponentId() == 1){
            this.pointNumber = 10.0;
        }else {
            Random random = new Random();
            // Sinh số ngẫu nhiên từ 1 đến 10
            int min = 4; // Số nhỏ nhất trong phạm vi
            int max = 11; // Số lớn nhất trong phạm vi

            // Sinh số nguyên ngẫu nhiên từ 4 đến 10
            int randomNumber = random.nextInt(max - min) + min;
            this.pointNumber = (double) randomNumber;
        }
    }

    public StudentsScores() {
    }
}
