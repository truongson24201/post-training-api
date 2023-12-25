package apisystem.posttraining.repository;

import apisystem.posttraining.entity.StudentsScores;
import apisystem.posttraining.entity.id.StudentScoreID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentsScoresRepository extends JpaRepository<StudentsScores, StudentScoreID> {
    @Query("SELECT ss FROM StudentsScores ss WHERE ss.regisClass.id = :regisClass")
    StudentsScores findByRegisClass(@Param("regisClass") Long regisClass);

    @Query("SELECT ss FROM StudentsScores ss WHERE ss.regisClass.id in :regisId ")
    List<StudentsScores> findAllByRegisIDs(@Param("regisId") List<Long> regisId);

    @Query("SELECT ss FROM StudentsScores ss WHERE ss.regisClass.id in :regisId " +
            "AND ss.componentSubject.component.componentId <> 4")
    List<StudentsScores> findAllNoFinalScore(@Param("regisId") List<Long> regisId);

    @Query("SELECT ss FROM StudentsScores ss WHERE ss.regisClass.id = :regisId " +
            "AND ss.componentSubject.component.componentId = 1")
    StudentsScores findStudentByAttendance(@Param("regisId") Long regisId);

    @Query("SELECT ss FROM StudentsScores ss WHERE ss.regisClass.id in :regisId " +
            "AND ss.componentSubject.component.componentId = 4")
    List<StudentsScores> findAllFinalScore(@Param("regisId") List<Long> regisId);

    @Query("SELECT ss FROM StudentsScores ss WHERE ss.regisClass.id = :regisId ")
    List<StudentsScores> findAllByRegisID(@Param("regisId") Long regisId);
}
