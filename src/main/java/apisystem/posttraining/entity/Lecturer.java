package apisystem.posttraining.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lecturer")
@Data
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecturer_id")
    private Long lecturerId;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "faculty_id",referencedColumnName = "faculty_id")
    private Faculty faculty;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "profile_id",referencedColumnName = "profile_id")
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id",referencedColumnName = "account_id")
    private Account account;

    @OneToMany(mappedBy = "lecturer")
    private List<Class> classes;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "lecturers")
    private List<Exam> exams;

}
