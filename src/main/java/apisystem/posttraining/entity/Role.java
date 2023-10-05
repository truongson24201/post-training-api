package apisystem.posttraining.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name",unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts;

}
