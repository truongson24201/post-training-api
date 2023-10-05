package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "behavior_criteria_sub")
@Data
public class BCriteriaSub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bcriteria_sub_id")
    private Long bCriteriaSubId;

    @Lob
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "max_point")
    private Integer max_point;

    @Column(name = "isDelete")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bcriteria_id",referencedColumnName = "bcriteria_id")
    private BCriteria bCriteria;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "criteriaSubs")
    private List<BSPattern> bSPatterns;
}
