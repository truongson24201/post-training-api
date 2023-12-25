package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ComponentPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentPointRepository extends JpaRepository<ComponentPoint,Long> {
    @Query("SELECT cp FROM ComponentPoint cp WHERE cp.name = :name")
    ComponentPoint findByName(@Param("name") String name);
}
