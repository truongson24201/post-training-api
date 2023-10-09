package apisystem.posttraining.repository;

import apisystem.posttraining.entity.RewardDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardDisciplineRepository extends JpaRepository<RewardDiscipline,Long> {
}
