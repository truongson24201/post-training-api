package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Graduation;
import apisystem.posttraining.entity.enumreration.EGraduation;
import apisystem.posttraining.entity.id.GraduationID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GraduationRepository extends JpaRepository<Graduation, Long> {
    @Query("SELECT g FROM Graduation g WHERE g.graduationType = 'GraduationInternship' " +
            "AND g.makeYear = :makeYear AND g.result = :result")
    List<Graduation> findAllByMakeYear(@Param("makeYear") String makeYear,@Param("result") boolean result);

    @Query("SELECT g FROM Graduation g WHERE g.graduationType = :graduationType AND g.creditsStudents.student.aClass.faculty.facultyId = :facultyId " +
            "AND g.makeYear = :makeYear ")
    List<Graduation> findAllByMakeYearAndGradutation(@Param("makeYear") String makeYear,
                                                     @Param("facultyId") Long facultyId,
                                                     @Param("graduationType") EGraduation graduation);
//
//    @Query("SELECT DISTINCT g FROM Graduation g " +
//            "WHERE g.creditsStudents.student.status = 'dang_hoc' " +
//            "AND g.creditsStudents.classCredit.faculty.facultyId = :facultyId " +
//            "AND g.graduationType = 'GraduationInternship' " +
//            "AND g.result = true " +
//            "AND g NOT IN " +
//            "(SELECT g2 FROM Graduation g2 " +
//            "WHERE g.creditsStudents.student.status = 'dang_hoc' " +
//            "AND g.creditsStudents.classCredit.faculty.facultyId = :facultyId " +
//            "AND g2.graduationType = 'GraduationThesis' " +
//            "AND g2.result = true " +
//            "AND g2.creditsStudents.classCredit.subject.name = 'Tốt nghiệp' )")

    @Query("SELECT DISTINCT g FROM Graduation g " +
            "WHERE g.creditsStudents.student.status = 'dang_hoc' " +
            "AND g.creditsStudents.classCredit.faculty.facultyId = :facultyId " +
            "AND g.graduationType = 'GraduationInternship' " +
            "AND g.result = true ")
    List<Graduation> findAllTrueGraduationInternship(@Param("facultyId") Long facultyId);

//    @Query("SELECT DISTINCT g FROM Graduation g " +
//            "WHERE g.classCredit.subject.name = 'Thực tập' " +
//            "AND g.result = true " +
//            "AND g.graduationType = 'GraduationInternship' " +
//            "AND g NOT IN " +
//            "(SELECT g2 FROM Graduation g2 " +
//            "WHERE g2.classCredit.subject.name = 'Tốt nghiệp' " +
//            "AND g2.result = true " +
//            "AND g2.student IN " +
//            "(SELECT g3.student FROM Graduation g3 " +
//            "WHERE 'Thay thế' IN (SELECT cc.classCredit.subject.name FROM g3.student.classCredits cc)))")
//    List<Graduation> findEligibleGraduations();

    @Query("SELECT DISTINCT g FROM Graduation g " +
            "JOIN g.creditsStudents.student s " +
            "JOIN g.creditsStudents.classCredit cc " +
            "WHERE cc.subject.name = 'Thực tập' " +
            "AND g.result = true " +
            "AND g.graduationType = 'GraduationInternship' " +
            "AND s NOT IN " +
            "(SELECT cs.student FROM ClassCreditsStudents cs " +
            "WHERE cs.classCredit.subject.name = 'Thay thế' " +
            "AND cs.student IN " +
            "(SELECT g2.creditsStudents.student FROM Graduation g2 " +
            "WHERE g2.creditsStudents.classCredit.subject.name = 'Tốt nghiệp' " +
            "AND g2.graduationType = 'GraduationThesis' " +
            "AND g2.result = true))")
    List<Graduation> findEligibleNonGraduatedStudents();

    @Query("SELECT g FROM Graduation g WHERE g.creditsStudents.id IN :regisId")
    List<Graduation> findAllClassCredit(@Param("regisId") List<Long> regisId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Graduation g WHERE g.creditsStudents.id IN :regisId ")
    void deleteAllClassCredit(@Param("regisId") List<Long> regisId);
}
