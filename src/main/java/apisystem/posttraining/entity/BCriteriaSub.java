package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "behavior_criteria_sub")
@Getter
@Setter
public class BCriteriaSub  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bcriteria_sub_id")
    private Long bCriteriaSubId;

    @Lob
    @Column(name = "name")
    @Nationalized
    private String name;

    @Lob
    @Column(name = "description")
    @Nationalized
    private String description;

//    @Column(name = "max_point")
//    private Integer max_point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bcriteria_id",referencedColumnName = "bcriteria_id")
    private BCriteria bCriteria;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "bCriteriaSub")
    private List<BSPatternContent> bSPContents;


    public BCriteriaSub(String name, String description, BCriteria bCriteria) {
        this.name = name;
        this.description = description;
        this.bCriteria = bCriteria;
    }


    public BCriteriaSub() {
    }
}
