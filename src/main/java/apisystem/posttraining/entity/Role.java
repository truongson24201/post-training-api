package apisystem.posttraining.entity;

import apisystem.posttraining.entity.enumreration.ERole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "name",unique = true)
    private ERole name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Set<Account> accounts;

}
