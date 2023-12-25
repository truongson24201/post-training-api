package apisystem.posttraining.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "classroom")
@Getter
@Setter
public class Classroom  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private Long classroomId;

    @Column(name = "name")
    private String name;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "capacity")
    private Integer capacity;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "classroom")
    private List<TimeTable> timeTables;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "classroom")
    private List<Exam> exams;
}
