package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "subject")
@Getter
@Setter
public class Subject  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Nationalized
    @Column(name = "name")
    private String name;

    @Column(name = "credit_num")
    private Integer creditNum;

    @Column(name = "theory_num")
    private Integer theoryNum;

    @Column(name = "practical_num")
    private Integer practicalNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_id")
    private Subject prerequisite;

    @ManyToMany(mappedBy = "subjects",fetch = FetchType.LAZY)
    private List<Curriculum> curriculums;

//   @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "components_subjects",
//            joinColumns = @JoinColumn(name = "subject_id"),
//            inverseJoinColumns = @JoinColumn(name = "component_id")
//    )
//    private List<ComponentPoint> componentPoints;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    private List<ComponentSubject> componentSubjects;
//
//    @OneToMany(fetch=FetchType.LAZY,mappedBy = "subject",cascade=CascadeType.ALL)
//    private List<ComponentSubject> componentSubjects;


}
