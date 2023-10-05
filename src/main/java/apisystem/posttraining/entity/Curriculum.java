package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

// Chuong trinh giang day
@Entity
@Table(name = "curriculum")
@Data
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculum_id")
    private Long curriculumId;

    @Column(name = "name")
    private String name;

    @Column(name = "numSubject")
    private Integer numSubject;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "faculty_id",referencedColumnName = "faculty_id")
    private Faculty faculty;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "curriculums_subjects",
            joinColumns = @JoinColumn(name = "curriculum_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;
}
