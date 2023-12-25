//package apisystem.posttraining.repository;
//
//import apisystem.posttraining.entity.ClassCredit;
//import apisystem.posttraining.entity.RegisExamPlan;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface RegisExamPlanRepository extends JpaRepository<RegisExamPlan, Long> {
//    @Query("SELECT rep FROM RegisExamPlan rep WHERE rep.examPlan.examPlanId = :examPlanId")
//    Optional<RegisExamPlan> findByExamPlanId(@Param("examPlanId") Long examPlanId);
//}
