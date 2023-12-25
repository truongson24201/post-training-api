package apisystem.posttraining.repository;

import apisystem.posttraining.entity.Curriculum;
import apisystem.posttraining.entity.RewardsStudents;
import apisystem.posttraining.entity.id.RewardDisSemesterID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardsStudentsRepository extends JpaRepository<RewardsStudents, Long> {
//    @Query("SELECT rs FROM RewardsStudents rs WHERE rs.curriculum IN :curriculums " +
//            "AND rs.reward.rewardDisciplineId = :rewardDisciplineId")
//    List<RewardsStudents> findAllSemester(@Param("curriculums") List<Curriculum> curriculums,
//                                          @Param("rewardDisciplineId") Long rewardDisciplineId);

    @Query("SELECT rs FROM RewardsStudents rs WHERE rs.semester.semesterId = :semesterId " +
            "AND rs.reward.rewardDisciplineId = :rewardDisciplineId")
    List<RewardsStudents> findAllBySemester(@Param("semesterId") Long semesterId,
                                            @Param("rewardDisciplineId") Long rewardDisciplineId);

    @Query("SELECT rs FROM RewardsStudents rs WHERE rs.semester.semesterId = :semesterId " +
            "AND rs.student.aClass.faculty.facultyId = :facultyId " +
            "AND rs.reward.rewardDisciplineId = :rewardDisciplineId ")
    List<RewardsStudents> findAllByOneSemester(@Param("facultyId") Long facultyId,
                                               @Param("rewardDisciplineId") Long rewardDisciplineId,
                                               @Param("semesterId") Long semesterId);

}
