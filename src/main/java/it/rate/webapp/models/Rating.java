package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
  @EmbeddedId private RatingId id;
  private int score;

  @ManyToOne
  @MapsId("userId")
  private User user;

  @ManyToOne
  @MapsId("placeId")
  private Place place;

  @ManyToOne
  @MapsId("criterionId")
  private Criterion criterion;

  public Rating(User user, Place place, Criterion criterion, int score) {
    this.id = new RatingId(user.getId(), place.getId(), criterion.getId());
    this.user = user;
    this.place = place;
    this.criterion = criterion;
    this.score = score;
  }
}
