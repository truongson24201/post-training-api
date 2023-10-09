package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ClassCreditsStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditsStudentsRepository extends JpaRepository<ClassCreditsStudents,Long> {
}
