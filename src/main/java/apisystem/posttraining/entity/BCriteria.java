package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "behavior_criteria")
@Data
public class BCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bcriteria_id")
    private Long bCriteriaId;

    @Lob
    @Column(name = "name")
    private String name;

    @Column(name = "max_point")
    private Double maxPoint;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "bCriteria")
    private List<BCriteriaSub> criteriaSubs;
}
