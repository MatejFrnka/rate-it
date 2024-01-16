package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "places")
public class Place {
  @Id @GeneratedValue private Long id;

  @Column(nullable = false)
  private String name;

  private String description;
  private String address;

  @Column(nullable = false)
  @Range(min = -90, max = 90)
  private double latitude;

  @Column(nullable = false)
  @Range(min = -180, max = 180)
  private Double longitude;

  @ElementCollection @Builder.Default private List<String> imageNames = new ArrayList<>();

  @Builder.Default private boolean deleted = false;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private AppUser creator;

  @ManyToOne private Interest interest;

  @OneToMany(mappedBy = "place")
  @Builder.Default
  private List<Rating> ratings = new ArrayList<>();

  public Double getAverageRating() {
    Double averageRating = null;
    OptionalDouble optAverageRating = ratings.stream().mapToDouble(Rating::getRating).average();
    if (optAverageRating.isPresent()) {
      averageRating = optAverageRating.getAsDouble();
    }
    return averageRating;
  }
}
