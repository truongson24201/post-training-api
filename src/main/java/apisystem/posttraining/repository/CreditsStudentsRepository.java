package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ClassCredit;
import apisystem.posttraining.entity.ClassCreditsStudents;
import apisystem.posttraining.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CreditsStudentsRepository extends JpaRepository<ClassCreditsStudents,Long> {
    @Query("SELECT cs FROM ClassCreditsStudents cs WHERE cs.classCredit.classCreditId = :classCreditId ")
    List<ClassCreditsStudents> findAllByClassCreditId(@Param("classCreditId") Long classCreditId);

    @Query("SELECT cs FROM ClassCreditsStudents cs WHERE cs.classCredit IN :classCredits ")
    List<ClassCreditsStudents> findAllByClassCredits(@Param("classCredits") List<ClassCredit> classCredits);

    @Query("SELECT cs FROM ClassCreditsStudents cs WHERE cs.classCredit.classCreditId = :classCreditId " +
            "AND cs.student IN :students")
    List<ClassCreditsStudents> findAllForExam(@Param("classCreditId") Long classCreditId,
                                              @Param("students") List<Student> students);

    @Query("SELECT cs FROM ClassCreditsStudents cs WHERE cs.student.studentId = :studentId")
    List<ClassCreditsStudents> findAllByStudentID(@Param("studentId") String studentId);

    @Query("SELECT cs FROM ClassCreditsStudents cs WHERE cs.id  IN :regisId")
    List<ClassCreditsStudents> findAllByID(@Param("regisId") List<Long> regisId);

    @Query("SELECT cs FROM ClassCreditsStudents cs " +
            "WHERE cs.classCredit.semester.semesterId = :semesterId " +
            "AND cs.student.studentId = :studentId")
    List<ClassCreditsStudents> findAllInSemester(@Param("semesterId") Long semesterId,
                                                      @Param("studentId") Long studentId);

    @Query("SELECT count(cc) FROM ClassCreditsStudents cc WHERE cc.classCredit.classCreditId = :classCreditId")
    int countAllByClassCreditId(@Param("classCreditId") Long classCreditId);

    @Query("SELECT cc FROM ClassCreditsStudents cc WHERE cc.classCredit.classCreditId = :classCreditId " +
            "AND cc.examStatus = true")
    List<ClassCreditsStudents> findAllStudentsExamTrue(@Param("classCreditId") Long classCreditId);

    @Query("SELECT cc FROM ClassCreditsStudents cc WHERE cc.classCredit.classCreditId = :classCreditId " +
            "AND cc.student.studentId = :studentId")
    ClassCreditsStudents findByStudent(@Param("classCreditId") Long classCreditId,
                                       @Param("studentId") String studentId);

    @Query("SELECT cs FROM ClassCreditsStudents cs WHERE cs.student.studentId In :studentId")
    List<ClassCreditsStudents> findAllByStudentIDs(@Param("studentId") List<String> studentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ClassCreditsStudents cc where cc.id IN :regisId")
    void deleteAllRegisId(@Param("regisId") List<Long> regisId);

    @Query("SELECT CASE WHEN COUNT(cc) > 0 THEN true ELSE false END " +
            "FROM ClassCreditsStudents cc WHERE cc.student.studentId = :studentId " +
            "AND cc.classCredit.subject.subjectId = :subjectId " +
            "AND cc.classCredit.semester.semesterId <> :semesterId")
    boolean findAllByNotInSemesterOfStudent(@Param("studentId") String studentId,
                                                               @Param("subjectId") Long subjectId,
                                                               @Param("semesterId") Long semesterId);

    @Modifying
    @Transactional
    @Query("UPDATE ClassCreditsStudents c SET c.status = :newStatus WHERE c.id = :id")
    void updateStatusById(@Param("newStatus") Boolean newStatus, @Param("id") Long id);

}
