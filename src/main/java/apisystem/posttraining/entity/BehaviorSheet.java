package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "behavior_sheet")
@Data
public class BehaviorSheet {
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
}
