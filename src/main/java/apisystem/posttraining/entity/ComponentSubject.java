package apisystem.posttraining.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "components_subjects",
    uniqueConstraints = @UniqueConstraint(columnNames = {"subject_id","component_id"})
)
@Getter
@Setter
//@IdClass(ComponentSubjectId.class)
public class ComponentSubject  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long comSubId;

//    @Id
    @JsonIgnore
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
