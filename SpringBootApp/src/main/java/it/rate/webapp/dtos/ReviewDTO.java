package it.rate.webapp.dtos;

import it.rate.webapp.models.Review;

import java.sql.Timestamp;

public record ReviewDTO(String text, Timestamp timestamp) {

  public ReviewDTO(Review review) {
    this(review.getText(), review.getCreatedAt());
  }
}
