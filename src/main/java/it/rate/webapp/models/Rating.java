package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
  @EmbeddedId private RatingId id;

  @Range(min = 1, max = 10)
  @Column(nullable = false)
  private int rating;

  @ManyToOne
  @MapsId("userId")
  private AppUser appUser;

  @ManyToOne
  @MapsId("placeId")
  private Place place;

  @ManyToOne
  @MapsId("criterionId")
  private Criterion criterion;

  public Rating(AppUser appUser, Place place, Criterion criterion, int rating) {
    this.id = new RatingId(appUser.getId(), place.getId(), criterion.getId());
    this.appUser = appUser;
    this.place = place;
    this.criterion = criterion;
    this.rating = rating;
  }

  public Long getCriterionId() {
    return criterion.getId();
  }
}
