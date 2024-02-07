package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import it.rate.webapp.models.Review;
import it.rate.webapp.models.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

  boolean existsByAppUserAndPlace(AppUser user, Place place);
}
