package apisystem.posttraining.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "behavior_criteria")
@Getter
@Setter
@RequiredArgsConstructor
// no calculation in the constructor, but
// since Lombok 1.18.16, we can cache the hash code
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class BCriteria  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bcriteria_id")
    @JsonProperty("bCriteriaId")
    private Long bCriteriaId;

    @Lob
    @Column(name = "name")
    @Nationalized
    private String name;

//    @Column(name = "max_point")
//    private Double maxPoint;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "bCriteria")
    private List<BCriteriaSub> bCriteriaSubs;

    public BCriteria(String name) {
        this.name = name;
    }

}
