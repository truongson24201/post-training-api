package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "behavior_sheet",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id", "semester_id","bs_pattern_id"})}
)
@Getter
@Setter
public class BehaviorSheet  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bsheet_id")
    private Long bSheetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",referencedColumnName = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bs_pattern_id",referencedColumnName = "bs_pattern_id")
    private BSPattern bSPattern;

    public BehaviorSheet(Student student, Semester semester, BSPattern bSPattern) {
        this.student = student;
        this.semester = semester;
        this.bSPattern = bSPattern;
    }

    public BehaviorSheet() {
    }
}
