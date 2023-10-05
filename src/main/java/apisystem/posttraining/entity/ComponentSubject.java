package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "components_subjects")
@Data
//@IdClass(ComponentSubjectId.class)
public class ComponentSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

//    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private ComponentPoint component;

    @Column(name = "percent_number")
    private Double percentNumber;
}
