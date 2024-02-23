package it.rate.webapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
  @EmbeddedId @NotNull private RatingId id;

  @Range(min = 1, max = 10)
  @Column(nullable = false)
  private Integer rating;

  @NotNull
  @Column(nullable = false)
  private final Timestamp createdAt = new Timestamp(System.currentTimeMillis());

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
    this.rating = Math.toIntExact(rating);
  }

  public Long getCriterionId() {
    return criterion.getId();
  }
}
