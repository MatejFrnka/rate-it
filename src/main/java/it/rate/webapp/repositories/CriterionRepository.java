package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Criterion;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CriterionRepository extends JpaRepository<Criterion, Long> {
  List<Criterion> findAllByInterestAndRatings_AppUserAndRatings_Place(
      Interest interest, AppUser appUser, Place place);
}
