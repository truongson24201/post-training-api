package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "time_table")
@Data
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long timeTableId;

    @Column(name = "date")
    private Date lessonDate;

    @Column(name = "status")
    private Boolean status;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "class_credit_id",referencedColumnName = "class_credit_id")
    private ClassCredit classCredit;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "classroom_id",referencedColumnName = "classroom_id")
    private Classroom classroom;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "timetable_shiftsystem",
            joinColumns = @JoinColumn(name = "timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_system_id"))
    private List<ShiftSystem> shiftSystems;

    // Diem Chuyen Can
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "attendance_score",
            joinColumns = @JoinColumn(name = "timetable_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;
}
