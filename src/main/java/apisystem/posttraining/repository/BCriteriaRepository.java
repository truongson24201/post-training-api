package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BCriteriaRepository extends JpaRepository<BCriteria,Long> {
}
