package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ComponentSubject;
import apisystem.posttraining.entity.id.ComponentSubjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentSubjectRepository extends JpaRepository<ComponentSubject, Long> {
    @Query("SELECT cs FROM ComponentSubject cs " +
            "WHERE cs.component.componentId = :componentId " +
            "AND cs.subject.subjectId = :subjectId")
    ComponentSubject findByAttendanceScore(@Param("componentId") Long componentId,
                                           @Param("subjectId") Long subjectId);

//    ComponentSubject findBySubjectId
}
