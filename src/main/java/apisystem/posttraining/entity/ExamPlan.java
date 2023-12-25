package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "exam_plan")
@Getter
@Setter
public class ExamPlan  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_plan_id")
    private Long examPlanId;

    @Column(name = "date_start",columnDefinition = "DATE")
    private LocalDate dateStart;

    @Column(name = "date_end",columnDefinition = "DATE")
    private LocalDate dateEnd;

    @Nationalized
    @Column(name = "title")
    private String title;

    @Column(name = "flag")
    private Boolean flag;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "semester_id",referencedColumnName = "semester_id")
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id",referencedColumnName = "employee_id")
    private Employee updateBy;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "examPlan")
    private List<ClassCredit> classCredits;

    public ExamPlan(LocalDate dateStart, LocalDate dateEnd, String title, Boolean flag, Semester semester, Boolean status) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.title = title;
        this.flag = flag;
        this.semester = semester;
        this.status = status;
    }

    public ExamPlan() {
    }
}
