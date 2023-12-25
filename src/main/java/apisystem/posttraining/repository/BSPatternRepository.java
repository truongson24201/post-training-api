package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BSPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BSPatternRepository extends JpaRepository<BSPattern,Long> {
    @Query("SELECT bp FROM BSPattern bp WHERE bp.status = true AND bp.bSPatternId <> :bSPatternId")
    List<BSPattern> getAllByStatusTrue(@Param("bSPatternId") Long bSPatternId);


    @Query("SELECT bp FROM BSPattern bp WHERE bp.dateOpen = :dateOpen")
    Optional<BSPattern> getByDateOpen(@Param("dateOpen") LocalDate dateOpen);

    Optional<BSPattern> findByStatusTrue();
    Optional<BSPattern> findByStatusFalse();

    @Query("SELECT bp FROM BSPattern bp WHERE bp.dateOpen = (SELECT MAX(b.dateOpen) FROM BSPattern b)")
    Optional<BSPattern> findFirstByDateOpenMax();
}
