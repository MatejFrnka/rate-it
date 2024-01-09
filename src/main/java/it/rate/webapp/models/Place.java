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
@Table(name = "places")
public class Place {
  @Id @GeneratedValue private Long id;

  @Column(nullable = false)
  private String name;

  private String description;
  private String address;
  private double latitude;
  private double longitude;

  @ElementCollection
  private List<String> imageNames = new ArrayList<>();

  @Builder.Default
  private boolean deleted = false;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private AppUser creator;

  @ManyToOne private Interest interest;

  @OneToMany(mappedBy = "place")
  @Builder.Default
  private List<Rating> ratings = new ArrayList<>();

  public double getAverageRating() {
    return ratings.stream().mapToDouble(Rating::getScore).average().orElse(-1);
  }
}
