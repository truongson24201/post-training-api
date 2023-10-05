package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

@Entity
@Table(name = "profile",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Nationalized
    @Column(name = "fullnanme")
    private String fullname;

    @Column(name = "DOB")
    private Date DOB;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

//    @Column(name = "citizen_identity")
//    private String  citizenIdentity;

    @OneToOne
    @JoinColumn(name = "citizen_identity", referencedColumnName = "citizen_identity", insertable = false, updatable = false)
    private MatriculateStudent matriculateStudent;

    @Nationalized
    @Column(name = "address")
    private String address;

    @Column(name = "qualification")
    private String qualification;
}
