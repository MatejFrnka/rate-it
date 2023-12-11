package it.rate.webapp.repositories;

import it.rate.webapp.models.Rating;
import it.rate.webapp.models.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
}
