package apisystem.posttraining.repository;

import apisystem.posttraining.entity.RewardDiscipline;
import apisystem.posttraining.entity.enumreration.ERDType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardDisciplineRepository extends JpaRepository<RewardDiscipline,Long> {
    @Query("SELECT r FROM RewardDiscipline r WHERE r.eRDType = :type")
    RewardDiscipline findByType(@Param("type") ERDType type);
}
