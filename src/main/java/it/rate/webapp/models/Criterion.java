package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "criteria")
public class Criterion {
  @Id @GeneratedValue private Long id;
  private String name;
  private boolean deleted = false;

  @ManyToOne private Interest interest;

  @OneToMany(mappedBy = "criterion", cascade = CascadeType.ALL)
  private List<Rating> ratings = new ArrayList<>();

  public double getAveragePlaceRating(Place place) {
    return ratings.stream()
        .filter(r -> r.getPlace().equals(place))
        .mapToDouble(Rating::getScore)
        .average()
        .orElse(-1);
    // todo: optimize - now filtering from all Ratings of given criterion,
    // asking the database for ratings of given criterion for specific place would be much more
    // efficient.
  }
}
