package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester,Long> {
    @Query("SELECT s FROM Semester s WHERE s.dateStart <= :currentDate " +
            "AND s.dateEnd >= :currentDate")
    Semester findCurrentSemester(@Param("currentDate")LocalDate currentDate);

    @Query("SELECT s FROM Semester s WHERE s.year IN :years")
    List<Semester> findAllYears(@Param("years") List<Integer> years);
}
