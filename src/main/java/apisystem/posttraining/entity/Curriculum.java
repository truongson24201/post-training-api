package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

// Chuong trinh giang day
@Entity
@Table(name = "curriculum",
    uniqueConstraints = @UniqueConstraint(columnNames = {"semester_id","faculty_id"})
)
@Getter
@Setter
public class Curriculum  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculum_id")
    private Long curriculumId;

    @Nationalized
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "faculty_id",referencedColumnName = "faculty_id")
    private Faculty faculty;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "curriculums_subjects",
            joinColumns = @JoinColumn(name = "curriculum_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "curriculum")
//    private List<RewardsStudents> students;
}
