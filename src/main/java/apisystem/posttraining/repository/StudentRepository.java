package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Student;
import apisystem.posttraining.entity.enumreration.EStudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,String> {
    @Query("SELECT s FROM Student s WHERE s.aClass.classId = :aClassId")
    List<Student> findAllByClassId(@Param("aClassId") Long aClassId);

    @Query("SELECT s FROM Student s WHERE s.aClass.classId = :aClassId")
    Page<Student> findByClassId(@Param("aClassId") Long aClassId, Pageable pageable);

    @Query("SELECT count(s) FROM Student s where s.aClass.classId = :classId")
    Integer countAllByAClass_ClassId(@Param("classId") Long classId);

    @Query("SELECT s FROM Student s WHERE s.status = :status AND s.aClass.faculty.facultyId = :facultyId ")
    List<Student> findAllStudentStatus(@Param("facultyId") Long facultyId,
                                       @Param("status") EStudentStatus status);

}
