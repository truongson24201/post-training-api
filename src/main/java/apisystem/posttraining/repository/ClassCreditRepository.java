package apisystem.posttraining.repository;

import apisystem.posttraining.entity.ClassCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassCreditRepository extends JpaRepository<ClassCredit,Long> {
}
