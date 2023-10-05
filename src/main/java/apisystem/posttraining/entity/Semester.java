package apisystem.posttraining.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "semester")
@Data
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long semesterId;

//    @JsonFormat(pattern = "dd/MM/yyyy'-'HH:mm", timezone = "Asia/Ho_Chi_Minh")
    @Column(name = "year")
    private Integer year;

    @Column(name = "name")
    private String name;

    @Column(name = "num")
    private Integer num;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "semester")
    private List<BehaviorSheet> bSheets;
}
