package it.rate.webapp.models;

import it.rate.webapp.config.SpringConfiguration;
import it.rate.webapp.repositories.RatingRepository;
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
    RatingRepository ratingRepository =
        (RatingRepository)
            SpringConfiguration.contextProvider()
                .getApplicationContext()
                .getBean("ratingRepository");
    List<Rating> ratings = ratingRepository.findAllByCriterionAndPlace(this, place);
    return ratings.stream().mapToDouble(Rating::getScore).average().orElse(-1);
  }
}
