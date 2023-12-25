package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ShiftSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftSystemRepository extends JpaRepository<ShiftSystem,Long> {
    @Query("SELECT ss FROM ShiftSystem ss WHERE ss.type = :shiftType")
    List<ShiftSystem> findAllByShiftType(@Param("shiftType") boolean shiftType);
}
