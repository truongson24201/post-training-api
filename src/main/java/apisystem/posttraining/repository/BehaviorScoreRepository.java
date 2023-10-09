package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BehaviorScore;
import apisystem.posttraining.entity.id.BSheetBCriteriaSubId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BehaviorScoreRepository extends JpaRepository<BehaviorScore, BSheetBCriteriaSubId> {
}
