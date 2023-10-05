package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "classroom")
@Data
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private Long classroomId;

    @Column(name = "name")
    private String name;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "max_size")
    private Integer maxSize;

//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "classroom")
//    private List<Exam> examList;
}
