package apisystem.posttraining.entity;

import apisystem.posttraining.entity.id.BSheetBCriteriaSubId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name = "behavior_score")
@IdClass(BSheetBCriteriaSubId.class)
@Getter
@Setter
public class BehaviorScore  implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "bsheet_id")
    private BehaviorSheet bSheet;

    @Id
    @ManyToOne
    @JoinColumn(name = "bcriteria_sub_id")
    private BCriteriaSub bCriteriaSub;

    @Column(name = "self_point")
    private Integer selfPoint;

    @Column(name = "class_point")
    private Integer classPoint;

    @Column(name = "advisor_point")
    private Integer advisorPoint;

    public BehaviorScore(BehaviorSheet bSheet, BCriteriaSub bCriteriaSub, Integer selfPoint, Integer classPoint, Integer advisorPoint) {
        this.bSheet = bSheet;
        this.bCriteriaSub = bCriteriaSub;
        this.selfPoint = selfPoint;
        this.classPoint = classPoint;
        this.advisorPoint = advisorPoint;
    }

    public BehaviorScore(BehaviorSheet bSheet, BCriteriaSub bCriteriaSub) {
        this.bSheet = bSheet;
        this.bCriteriaSub = bCriteriaSub;
    }

    @PrePersist
    private void setDefaultPoint(){
        Random random = new Random();

        // Sinh số ngẫu nhiên từ 1 đến 5 và gán cho các trường
        this.selfPoint = random.nextInt(5) + 1;
        this.classPoint = random.nextInt(5) + 1;
        this.advisorPoint = random.nextInt(5) + 1;
    }

    public BehaviorScore() {
    }
}
