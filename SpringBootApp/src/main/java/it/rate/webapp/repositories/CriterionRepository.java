package it.rate.webapp.repositories;

import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CriterionRepository extends JpaRepository<Criterion, Long> {
  List<Criterion> findAllByInterest(Interest interest);
}
