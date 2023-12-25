package apisystem.posttraining.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "account",uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
//@Data
@Getter
@Setter
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "username")
    private String username;

//    @OneToOne(mappedBy = "account",fetch = FetchType.LAZY) // Map với trường accountId trong Student
//    private Student student;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "username", referencedColumnName = "student_id")
//    private Student student;


    @Column(name = "password")
    private String password;

//    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(name = "accounts_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
