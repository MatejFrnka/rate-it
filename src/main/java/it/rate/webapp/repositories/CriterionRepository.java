package it.rate.webapp.repositories;

import it.rate.webapp.models.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriterionRepository extends JpaRepository<Criterion, Long> {
}
