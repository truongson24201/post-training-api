package apisystem.posttraining.entity;

import apisystem.posttraining.entity.id.BSPatternContentId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "bs_patterns_contents")
@Getter
@Setter
@IdClass(BSPatternContentId.class)
public class BSPatternContent  implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bs_pattern_id")
    private BSPattern bSPattern;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bcriteria_sub_id")
    private BCriteriaSub bCriteriaSub;

    @Column(name = "max_point")
    private Integer maxPoint;

    public BSPatternContent(BSPattern bSPattern, BCriteriaSub bCriteriaSub, Integer maxPoint) {
        this.bSPattern = bSPattern;
        this.bCriteriaSub = bCriteriaSub;
        this.maxPoint = maxPoint;
    }

    public BSPatternContent() {

    }
}
