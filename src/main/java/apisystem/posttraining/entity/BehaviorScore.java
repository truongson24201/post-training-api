package apisystem.posttraining.entity;

import apisystem.posttraining.entity.id.BSheetBCriteriaSubId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "behavior_score")
@IdClass(BSheetBCriteriaSubId.class)
@Data
public class BehaviorScore {
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


}
