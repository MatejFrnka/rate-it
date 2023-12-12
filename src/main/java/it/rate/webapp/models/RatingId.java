package it.rate.webapp.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RatingId implements Serializable {
  private Long userId;
  private Long placeId;
  private Long criterionId;
}
