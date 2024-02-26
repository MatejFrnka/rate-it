package it.rate.webapp.services;

import it.rate.webapp.config.Constraints;
import it.rate.webapp.dtos.ReviewDTO;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.ReviewRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
public class ReviewService {
  private final ReviewRepository reviewRepository;

  public Optional<Review> findById(ReviewId reviewId) {
    return reviewRepository.findById(reviewId);
  }

  public void deleteById(ReviewId reviewId) {
    reviewRepository.deleteById(reviewId);
  }

  public ReviewDTO save(
      @NotBlank @Length(max = Constraints.MAX_VARCHAR_LENGTH) String text,
      @Valid Place place,
      @Valid AppUser loggedUser) {
    ReviewId reviewId = new ReviewId(loggedUser.getId(), place.getId());
    Optional<Review> optReview = reviewRepository.findById(reviewId);

    if (optReview.isPresent()) {
      reviewRepository.deleteById(reviewId);
    }
    return new ReviewDTO(reviewRepository.save(new Review(loggedUser, place, text)));
  }
}
