package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ExamPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamPlanRepository extends JpaRepository<ExamPlan,Long> {

    @Query("SELECT ep FROM ExamPlan ep WHERE ep.semester.semesterId = :semesterId ")
    List<ExamPlan> findAllBySemesterId(@Param("semesterId") Long semesterId);

    @Query("SELECT ep FROM ExamPlan ep WHERE ep.status = true ")
    List<ExamPlan> findAllHasBuild();

    @Query("SELECT ep FROM ExamPlan ep WHERE ep.flag = true ")
    List<ExamPlan> findAllHasTurnOn();



}
