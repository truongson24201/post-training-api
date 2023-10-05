package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ManyToAny;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shift_system")
@Data
public class ShiftSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_system_id")
    private Integer shiftSystemId;

    @Column(name = "timeStart")
    private Date timeStart;

    @Column(name = "timeClose")
    private Date timeClose;

    @Column(name = "type")
    private Boolean type;

    @ManyToMany(mappedBy = "shiftSystems")
    private List<TimeTable> timeTable;

}
