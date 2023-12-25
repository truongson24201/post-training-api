package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
    @Query("SELECT cur FROM Curriculum cur WHERE cur.faculty.facultyId = :facultyId " +
            "AND cur.semester.semesterId = :semesterId")
    Curriculum findByFacultyAndSemester(@Param("facultyId") Long facultyId,
                                        @Param("semesterId") Long semesterId);

    @Query("SELECT cur FROM Curriculum cur WHERE cur.semester.semesterId = :semesterId")
    List<Curriculum> findAllBySemester(@Param("semesterId") Long semesterId);

}
