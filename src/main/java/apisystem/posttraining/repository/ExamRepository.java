package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {
    @Query("SELECT e FROM Exam e WHERE e.classCredit.examPlan.examPlanId = :examPlanId")
    List<Exam> findAllByExamPlanId(@Param("examPlanId") Long examPlanId);

    @Query("SELECT e FROM Exam e WHERE e.classCredit.examPlan.examPlanId = :examPlanId " +
            "AND e.classCredit.lecturer.account.accountId = :lecturerId")
    List<Exam> findAllByLecturer(@Param("examPlanId") Long examPlanId,
                                 @Param("lecturerId") Long lecturerId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Exam e " +
            "WHERE e.classCredit.classCreditId = :classCreditId " +
            "AND e.examDate = :examDate " +
            "AND e.shiftSystem.shiftSystemId = :shiftSystemId")
    boolean existsExamsByClassCreditAndDateAndShiftSystem(
            @Param("classCreditId") Long classCreditId,
            @Param("examDate") LocalDate examDate,
            @Param("shiftSystemId") Integer shiftSystemId);



}
