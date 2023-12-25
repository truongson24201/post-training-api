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
@Table(name = "faculty")
@Getter
@Setter
public class Faculty  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Long facultyId;

    @Nationalized
    @Column(name = "name")
    private String name;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "faculty")
    private List<Class> classes;
}
