package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BCriteriaSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BCriteriaSubRepository extends JpaRepository<BCriteriaSub,Long> {
    @Query("SELECT bcsub FROM BCriteriaSub bcsub WHERE bcsub.bCriteria.bCriteriaId = :bCriteriaId")
    List<BCriteriaSub> findAllByBCriteriaId(@Param("bCriteriaId") Long bCriteriaId);

    @Query("SELECT bcsub FROM BCriteriaSub bcsub WHERE bcsub.bCriteriaSubId IN :bCriteriaSubId")
    BCriteriaSub findAllByIds(@Param("bCriteriaSubId") Long bCriteriaSubIds);
}
