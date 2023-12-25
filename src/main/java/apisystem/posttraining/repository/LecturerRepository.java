package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer,Long> {
    @Query("SELECT l FROM Lecturer l WHERE l.account.accountId = :accountId")
    Lecturer findByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT DISTINCT le FROM Lecturer le " +
            "JOIN le.classCredits cc " +
            "JOIN cc.timeTables t " +
            "JOIN t.shiftSystems s " +
            "WHERE t.lessonDate = :datee " +
            "AND s.timeStart <= :endTime AND s.timeClose >= :startTime")
    List<Lecturer> findBusyLecturersByTimetable(
            @Param("datee") LocalDate datee,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT DISTINCT le FROM Lecturer le " +
            "WHERE le NOT IN (" +
            "    SELECT DISTINCT l FROM Lecturer l " +
            "    JOIN l.classCredits cc " +
            "    JOIN cc.timeTables t " +
            "    JOIN t.shiftSystems s " +
            "    WHERE t.lessonDate = :datee " +
            "    AND (s.timeStart <= :endTime AND s.timeClose >= :startTime)" +
            ")")
    List<Lecturer> findFreeLecturersByTimetable(
            @Param("datee") LocalDate datee,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT DISTINCT le FROM Lecturer le " +
            "JOIN le.classCredits cc " +
            "JOIN cc.exams e " +
            "JOIN e.shiftSystem es " +
            "WHERE e.examDate = :datee " +
            "AND es.timeStart <= :endTime AND es.timeClose >= :startTime")
    List<Lecturer> findBusyLecturersByExam(
            @Param("datee") LocalDate datee,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT DISTINCT le FROM Lecturer le " +
            "WHERE le NOT IN (" +
            "    SELECT DISTINCT l FROM Lecturer l " +
            "    JOIN l.exams e " +
            "    JOIN e.shiftSystem es " +
            "    WHERE e.examDate = :datee " +
            "    AND (es.timeStart <= :endTime AND es.timeClose >= :startTime)" +
            ")")
    List<Lecturer> findFreeLecturersByExam(
            @Param("datee") LocalDate datee,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT DISTINCT le FROM Lecturer le " +
            "WHERE le NOT IN (" +
            "    SELECT DISTINCT l FROM Lecturer l " +
            "    JOIN l.classCredits cc " +
            "    JOIN cc.timeTables t " +
            "    JOIN t.shiftSystems s " +
            "    WHERE t.lessonDate = :datee " +
            "    AND (s.timeStart <= :endTime AND s.timeClose >= :startTime)" +
            ") " +
            "AND le NOT IN (" +
            "    SELECT DISTINCT l FROM Lecturer l " +
            "    JOIN l.exams e " +
            "    JOIN e.shiftSystem es " +
            "    WHERE e.examDate = :datee " +
            "    AND (es.timeStart <= :endTime AND es.timeClose >= :startTime)" +
            ")")
    List<Lecturer> findFreeLecturersByTimetableAndExam(
            @Param("datee") LocalDate datee,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

}
