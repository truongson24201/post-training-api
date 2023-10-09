package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BCriteriaSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BCriteriaSubRepository extends JpaRepository<BCriteriaSub,Long> {
}
