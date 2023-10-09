package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ExamPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPlanRepository extends JpaRepository<ExamPlan,Long> {
}
