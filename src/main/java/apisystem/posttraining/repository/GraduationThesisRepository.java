//package apisystem.posttraining.repository;
//
//import apisystem.posttraining.entity.GraduationThesis;
//import apisystem.posttraining.entity.Student;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface GraduationThesisRepository extends JpaRepository<GraduationThesis, String> {
//    @Query("SELECT gt FROM GraduationThesis gt WHERE gt.result = :result")
//    List<GraduationThesis> findAllByResult(@Param("result") Boolean result);
//}
