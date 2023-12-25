package apisystem.posttraining.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "behavior_score_pattern")
@Getter
@Setter
public class BSPattern  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bs_pattern_id")
    private Long bSPatternId;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_open")
    private LocalDate dateOpen;

//    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_close")
    private LocalDate dateClose;

    @Column(name = "update_on")
    private LocalDate updateOn;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "status")
    private Boolean status;



    @OneToMany(mappedBy = "bSPattern",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<BSPatternContent> bsPatternContents;

    public BSPattern(Long bSPatternId, Boolean status, LocalDate updateOn, String updateBy) {
        this.bSPatternId = bSPatternId;
        this.status = status;
        this.updateOn = updateOn;
        this.updateBy = updateBy;
    }

    public BSPattern() {
    }

    //    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
//    @JoinTable(name = "bs_pattern_contents",
//            joinColumns = @JoinColumn(name = "bs_pattern_id"),
//            inverseJoinColumns = @JoinColumn(name = "bcriteria_sub_id"))
//    private List<BCriteriaSub> criteriaSubs;

}
