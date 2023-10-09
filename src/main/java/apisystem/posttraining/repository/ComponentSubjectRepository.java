package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ComponentSubject;
import apisystem.posttraining.entity.id.ComponentSubjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentSubjectRepository extends JpaRepository<ComponentSubject, ComponentSubjectId> {
}
