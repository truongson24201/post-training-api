package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BehaviorScore;
import apisystem.posttraining.entity.BehaviorSheet;
import apisystem.posttraining.entity.id.BSheetBCriteriaSubId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BehaviorScoreRepository extends JpaRepository<BehaviorScore, BSheetBCriteriaSubId> {
    @Query("SELECT bscore FROM BehaviorScore bscore WHERE bscore.bSheet.bSheetId = :bSheetId")
    List<BehaviorScore> findAllBSheet(@Param("bSheetId") Long bSheet);


}
