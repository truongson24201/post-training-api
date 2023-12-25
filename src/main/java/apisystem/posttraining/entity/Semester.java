package apisystem.posttraining.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "semester",
    uniqueConstraints = @UniqueConstraint(columnNames = {"year","num"})
)
@Getter
@Setter
public class Semester  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long semesterId;

//    @JsonFormat(pattern = "dd/MM/yyyy'-'HH:mm", timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "year")
    private Integer year;

    @Column(name = "num")
    private Integer num;

    @Column(name = "date_start",columnDefinition = "DATE")
    private LocalDate dateStart;

//    @Temporal(TemporalType.DATE)
    @Column(name = "date_end",columnDefinition = "DATE")
    private LocalDate dateEnd;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "semester")
    private List<BehaviorSheet> bSheets;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "semester")
    private List<ExamPlan> examPlans;

    public Semester(Integer year, Integer num, LocalDate dateStart, LocalDate dateEnd) {
        this.year = year;
        this.num = num;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public Semester() {
    }
}
