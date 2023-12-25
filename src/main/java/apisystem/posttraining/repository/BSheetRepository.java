package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BehaviorSheet;
import apisystem.posttraining.entity.Semester;
import apisystem.posttraining.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BSheetRepository extends JpaRepository<BehaviorSheet,Long> {
    @Query("SELECT bsheet FROM BehaviorSheet bsheet " +
            "WHERE bsheet.semester.semesterId = :semesterId " +
            "AND bsheet.student in :students")
    List<BehaviorSheet> findAllSemesterAndStudents(@Param("semesterId") Long semesterId, @Param("students") List<Student> students);

    @Query("SELECT bsheet FROM BehaviorSheet bsheet " +
            "WHERE bsheet.semester.semesterId = :semesterId " +
            "AND bsheet.student.studentId = :studentId")
    BehaviorSheet findSemesterAndStudent(@Param("semesterId") Long semesterId,
                                         @Param("studentId") String studentId);

    @Query("SELECT 1 FROM BehaviorSheet bsheet WHERE bsheet.bSPattern.bSPatternId = :bSPatternId")
    Optional<BehaviorSheet> checkPatternInBSheet(@Param("bSPatternId") Long bSPatternId);

    @Query("SELECT bsheet FROM BehaviorSheet bsheet " +
            "WHERE bsheet.semester.semesterId = :semesterId " +
            "AND bsheet.student.studentId = :studentId")
    BehaviorSheet findStudentAndSemester(@Param("semesterId") Long semesterId,
                                         @Param("studentId") String username);

    @Query("SELECT bsheet FROM BehaviorSheet bsheet " +
            "WHERE bsheet.semester.semesterId = :semesterId ")
    List<BehaviorSheet> findFirstBySemesterIs(@Param("semesterId") Long semesterId);
}
