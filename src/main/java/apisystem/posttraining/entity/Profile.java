package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "profile",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
public class Profile  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Nationalized
    @Column(name = "fullnanme")
    private String fullname;

    @Column(name = "DOB",columnDefinition = "DATE")
    private LocalDate DOB;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "citizen_identity",unique = true)
    private String  citizenIdentity;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "citizen_identity", referencedColumnName = "citizen_identity", insertable = false, updatable = false)
//    private MatriculateStudent matriculateStudent;

    @Nationalized
    @Column(name = "address")
    private String address;

    @Column(name = "qualification")
    private String qualification;
}
