package apisystem.posttraining.repository;

import apisystem.posttraining.entity.StudentsScores;
import apisystem.posttraining.entity.id.StudentScoreID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsScoresRepository extends JpaRepository<StudentsScores, StudentScoreID> {
}
