package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ComponentPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentPointRepository extends JpaRepository<ComponentPoint,Long> {
}
