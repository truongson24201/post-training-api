package apisystem.posttraining.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "component_point")
@Getter
@Setter
public class ComponentPoint  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_id")
    private Long componentId;

    @Nationalized
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    @Nationalized
    private String description;
//
//    @JsonIgnore
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @ManyToMany(mappedBy = "componentPoints",fetch = FetchType.LAZY)
//    private List<Subject> subjects;
}
