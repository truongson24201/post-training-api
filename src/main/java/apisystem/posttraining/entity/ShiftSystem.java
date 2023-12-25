package apisystem.posttraining.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ManyToAny;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shift_system")
@Getter
@Setter
@RequiredArgsConstructor
// no calculation in the constructor, but
// since Lombok 1.18.16, we can cache the hash code
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public class ShiftSystem  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_system_id")
    @JsonProperty("shiftSystemId")
    private Integer shiftSystemId;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "time_start", columnDefinition = "TIME")
    private LocalTime timeStart;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "time_close",columnDefinition = "TIME")
    private LocalTime timeClose;

    @Column(name = "type")
    private Boolean type;

//    @ManyToMany(mappedBy = "shiftSystems",fetch = FetchType.LAZY)
//    private List<TimeTable> timeTable;

}
