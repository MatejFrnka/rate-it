package it.rate.webapp.repositories;

import it.rate.webapp.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
  List<Rating> findAllByAppUserAndPlace(AppUser appUser, Place place);

  List<Rating> findAllByCriterionAndPlace(Criterion criterion, Place place);
}
