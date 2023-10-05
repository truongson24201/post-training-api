package apisystem.posttraining.entity;


import apisystem.posttraining.entity.enumreration.ERDType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rewar_discipline")
@Data
public class RewardDiscipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_discipline_id")
    private Long rewardDisciplineId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ERDType eRDType;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_id",referencedColumnName = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

}
