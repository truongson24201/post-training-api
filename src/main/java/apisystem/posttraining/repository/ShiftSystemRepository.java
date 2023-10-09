package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ShiftSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftSystemRepository extends JpaRepository<ShiftSystem,Long> {
}
