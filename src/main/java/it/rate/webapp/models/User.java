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
@Table(name = "users")
public class User {
  @Id @GeneratedValue private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "creator")
  private List<Place> createdPlaces = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Role> roles = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Vote> votes = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Rating> ratings = new ArrayList<>();
}
