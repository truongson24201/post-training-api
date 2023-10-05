package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exam_plan")
@Data
public class ExamPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_plan_id")
    private Long examPlanId;

    @Column(name = "regis_opening")
    private Date regisOpening;

    @Column(name = "regis_closing")
    private Date regisClosing;

    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "date_end")
    private Date dateEnd;

    @Column(name = "title")
    private String title;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "examPlan")
    private List<Exam> exams;

}
