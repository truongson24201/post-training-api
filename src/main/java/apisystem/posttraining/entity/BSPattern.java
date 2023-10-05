package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "behavior_score_pattern")
@Data
public class BSPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bs_pattern_id")
    private Long bSPatternId;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "bs_pattern_contents",
            joinColumns = @JoinColumn(name = "bs_pattern_id"),
            inverseJoinColumns = @JoinColumn(name = "bcriteria_sub_id"))
    private List<BCriteriaSub> criteriaSubs;


}
