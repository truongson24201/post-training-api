package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Role;
import apisystem.posttraining.entity.enumreration.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
