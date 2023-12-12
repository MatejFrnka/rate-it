package it.rate.webapp.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

  @ManyToOne private User creator;

  @ManyToOne private Interest interest;

  @OneToMany(mappedBy = "place")
  private List<Rating> ratings = new ArrayList<>();
}
