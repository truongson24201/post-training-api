package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;


// sinh vien trung tuyen
@Entity
@Table(name = "matriculate_student")
@Data
public class MatriculateStudent {
    @Id
    @Column(name = "citizen_identity")
//    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
//    @JoinColumn(name = "citizen_identity",referencedColumnName = "citizen_identity")
    private String citizenIdentity;

    @Column(name = "combined_exam_id")
    private Integer combinedExamId;

    @Column(name = "result_score")
    private Double resultScore;


}
