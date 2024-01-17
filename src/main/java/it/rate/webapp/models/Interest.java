package it.rate.webapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interests")
public class Interest {
  @Id @GeneratedValue private Long id;

  @NotBlank
  @Column(nullable = false)
  private String name;

  @NotBlank
  private String description;
  @Builder.Default
  private boolean deleted = false;
  private boolean exclusive;

  @OneToMany(mappedBy = "interest")
  @Builder.Default
  private List<Place> places = new ArrayList<>();

  @OneToMany(mappedBy = "interest")
  @Builder.Default
  private List<Criterion> criteria = new ArrayList<>();

  @OneToMany(mappedBy = "interest")
  @Builder.Default
  private List<Like> likes = new ArrayList<>();

  @OneToMany(mappedBy = "interest")
  @Builder.Default
  private List<Role> roles = new ArrayList<>();

  public int countLikes() {
    return likes.size();
  }
}
