package it.rate.webapp.repositories;

import it.rate.webapp.models.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CriterionRepository extends JpaRepository<Criterion, Long> {

  @Transactional
  void deleteByNameAndInterestId(String name, Long id);
}
