package apisystem.posttraining.repository;

import apisystem.posttraining.entity.BehaviorSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BSheetRepository extends JpaRepository<BehaviorSheet,Long> {
}
