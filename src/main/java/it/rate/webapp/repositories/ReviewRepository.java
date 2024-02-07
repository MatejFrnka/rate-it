package it.rate.webapp.repositories;

import it.rate.webapp.models.Review;
import it.rate.webapp.models.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {}
