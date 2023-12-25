package apisystem.posttraining.entity;


import apisystem.posttraining.entity.enumreration.ERDType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "reward_discipline")
@Getter
@Setter
public class RewardDiscipline  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_discipline_id")
    private Long rewardDisciplineId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ERDType eRDType;

    @Lob
    @Column(name = "content")
    @Nationalized
    private String content;


}
