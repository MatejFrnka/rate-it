package it.rate.webapp.repositories;

import it.rate.webapp.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

  List<Rating> findAllByCriterionAndPlaceAndCriterionDeletedFalse(Criterion criterion, Place place);

  List<Rating> findAllByAppUserAndPlaceAndCriterionDeletedFalse(AppUser appUser, Place place);
}
