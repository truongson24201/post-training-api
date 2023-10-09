package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BSPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BSPatternRepository extends JpaRepository<BSPattern,Long> {
}
