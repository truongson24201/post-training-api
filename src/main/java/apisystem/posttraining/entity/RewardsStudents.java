package apisystem.posttraining.entity;


import apisystem.posttraining.entity.id.RewardDisSemesterID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@IdClass(RewardDisSemesterID.class)
@Entity
@Table(name = "reward_discriplines_students")
public class RewardsStudents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rd_student_id")
    private Long rdStudentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_discipline_id")
    private RewardDiscipline reward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "gpa_found")
    private Double gpaFound;

    @Column(name = "gpa_behavior")
    private Integer gpaBehavior;

    @Column(name = "content")
    private String content;

    public RewardsStudents(RewardDiscipline reward, Student student, Semester semester, Double gpaFound, Integer gpaBehavior, String content) {
        this.reward = reward;
        this.student = student;
        this.semester = semester;
        this.gpaFound = gpaFound;
        this.gpaBehavior = gpaBehavior;
        this.content = content;
    }

    public RewardsStudents() {
    }
}
