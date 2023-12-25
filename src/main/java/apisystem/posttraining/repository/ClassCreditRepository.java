package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ClassCredit;
import apisystem.posttraining.entity.Semester;
import apisystem.posttraining.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassCreditRepository extends JpaRepository<ClassCredit, Long> {
    @Query("SELECT cc FROM ClassCredit cc " +
            "where cc.semester.semesterId = :semesterId " +
            "and cc.lecturer.lecturerId = :lecturerId and cc.isCompleted = true and cc.examPlan is null AND cc.status = 'Open' ")
    List<ClassCredit> findAllByCurSemester(@Param("semesterId") Long semesterId,
                                           @Param("lecturerId") Long lecturerId);

    @Query("SELECT cc FROM ClassCredit cc WHERE cc.classCreditId = :classCreditId " +
            "AND cc.lecturer.lecturerId = :lecturerId AND cc.status = 'Open'")
    Optional<ClassCredit> findByLecturer(@Param("classCreditId") Long classCreditId,
                                         @Param("lecturerId") Long lecturerId);

    @Query("SELECT cc FROM ClassCredit cc " +
            "where cc.semester.semesterId = :semesterId and cc.isCompleted = true and cc.examPlan is null AND cc.status = 'Open'")
    List<ClassCredit> findAllByCurSemester(@Param("semesterId") Long semesterId);

    @Query("SELECT cc FROM ClassCredit cc " +
            "where cc.semester.semesterId = :semesterId " +
            "and cc.lecturer.lecturerId = :lecturerId AND cc.status = 'Open'")
    List<ClassCredit> findAllToEnterPoint(@Param("semesterId") Long semesterId,
                                          @Param("lecturerId") Long lecturerId);

    @Query("SELECT cc FROM ClassCredit cc " +
            "where cc.semester.semesterId = :semesterId AND cc.status = 'Open'")
    List<ClassCredit> findAllToEnterPoint(@Param("semesterId") Long semesterId);

    @Query("SELECT cc FROM ClassCredit cc WHERE cc.status = 'Open'")
    List<ClassCredit> findAllByOpen();

    @Query("SELECT cc FROM ClassCredit cc " +
            "where cc.examPlan.examPlanId = :examPlanId")
    List<ClassCredit> findAllByExamPlanId(@Param("examPlanId") Long examPlanId);

    @Query("SELECT cc FROM ClassCredit cc WHERE cc.subject IN :subjects")
    List<ClassCredit> findAllBySubjects(@Param("subjects") List<Subject> subjects);

    @Query("SELECT cc FROM ClassCredit cc WHERE cc.faculty.facultyId = :facultyId " +
            "AND cc.semester IN :semesters")
    List<ClassCredit> findAllByFacultyIdAndSemesters(@Param("facultyId") Long facultyId,
                                                     @Param("semesters") List<Semester> semesters);

    @Query("SELECT cc FROM ClassCredit cc WHERE cc.faculty.facultyId = :facultyId " +
            "AND cc.semester.semesterId = :semesterId AND cc.status = 'Open'")
    List<ClassCredit> findAllByFacultyIdAndSemester(@Param("facultyId") Long facultyId,
                                                    @Param("semesterId") Long semesterId);

    @Query("SELECT cc FROM ClassCredit cc WHERE cc.faculty.facultyId = :facultyId " +
            "AND cc.semester.semesterId = :semesterId AND cc.subject.name = :name")
    Optional<ClassCredit> findCCInSeAndFacAndSub(@Param("facultyId") Long facultyId,
                                       @Param("semesterId") Long semesterId,
                                       @Param("name") String name);
}
