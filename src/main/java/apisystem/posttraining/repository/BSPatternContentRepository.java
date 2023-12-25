package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BSPatternContent;
import apisystem.posttraining.entity.id.BSPatternContentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BSPatternContentRepository extends JpaRepository<BSPatternContent, BSPatternContentId> {
    @Query("SELECT bsc FROM BSPatternContent bsc WHERE bsc.bSPattern.bSPatternId =:bSPatternId")
    List<BSPatternContent> findAllByBSPatternID(@Param("bSPatternId") Long bSPatternId);

    @Query("SELECT bsc FROM BSPatternContent bsc WHERE bsc.bSPattern.bSPatternId = :bSPatternId " +
            "AND bsc.bCriteriaSub.bCriteriaSubId = :bCriteriaSubId")
    BSPatternContent findOneMaxPoint(@Param("bSPatternId") Long bSPatternId,
                                     @Param("bCriteriaSubId") Long bCriteriaSubId);
}
