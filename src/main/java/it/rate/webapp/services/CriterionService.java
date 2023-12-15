package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.CriterionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CriterionService {
  private CriterionRepository criterionRepository;

  public List<Criterion> findAllByInterestAppUserPlace(
      Interest interest, AppUser appUser, Place place) {
    return criterionRepository.findAllByInterestAndRatings_AppUserAndRatings_Place(
        interest, appUser, place);
  }
}
