package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import it.rate.webapp.models.Rating;
import it.rate.webapp.models.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    List<Rating> findAllByAppUserAndPlace(AppUser appUser, Place place);
}
