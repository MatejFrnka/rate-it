package it.rate.webapp.models;

import it.rate.webapp.config.security.ServerRole;
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
public class AppUser {
  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ServerRole serverRole;

  @OneToMany(mappedBy = "creator")
  @Builder.Default
  private List<Place> createdPlaces = new ArrayList<>();

  @OneToMany(mappedBy = "appUser", fetch = FetchType.EAGER)
  @Builder.Default
  private List<Role> roles = new ArrayList<>();

  @OneToMany(mappedBy = "appUser")
  @Builder.Default
  private List<Like> likes = new ArrayList<>();

  @OneToMany(mappedBy = "appUser")
  @Builder.Default
  private List<Rating> ratings = new ArrayList<>();
}
