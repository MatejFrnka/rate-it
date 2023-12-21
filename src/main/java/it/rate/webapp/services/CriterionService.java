package it.rate.webapp.services;

import it.rate.webapp.models.Criterion;
import it.rate.webapp.repositories.CriterionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CriterionService {
  private CriterionRepository criterionRepository;

  public Optional<Criterion> findById(Long id) {
    return criterionRepository.findById(id);
  }
}
