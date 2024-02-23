package it.rate.webapp.models;

import it.rate.webapp.config.Constraints;
import it.rate.webapp.dtos.InterestInDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interests")
public class Interest {

  @Id @GeneratedValue @NotNull private Long id;

  @NotBlank
  @Length(min = Constraints.MIN_NAME_LENGTH, max = Constraints.MAX_NAME_LENGTH)
  @Column(nullable = false)
  private String name;

  @NotBlank
  @Length(max = Constraints.MAX_DESCRIPTION_LENGTH)
  @Column(nullable = false, length = 1000)
  private String description;

  @Builder.Default private boolean deleted = false;

  private boolean exclusive;

  private String imageName;

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

  @ManyToMany
  @Builder.Default
  @JoinTable(
      name = "interest_category",
      joinColumns = @JoinColumn(name = "interest_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private List<Category> categories = new ArrayList<>();

  public Interest(InterestInDTO interestDTO) {
    this.name = interestDTO.name();
    this.description = interestDTO.description();
    this.exclusive = interestDTO.exclusive();
    this.imageName = interestDTO.imageName();
  }

  public void update(InterestInDTO interestDTO) {
    this.name = interestDTO.name();
    this.description = interestDTO.description();
    this.exclusive = interestDTO.exclusive();
    this.imageName = interestDTO.imageName();
  }

  public int countLikes() {
    return likes.size();
  }

  public List<Long> getCategoryIds() {
    return categories.stream().map(Category::getId).collect(Collectors.toList());
  }

  public int countPlaces() {
    return places.size();
  }

  public List<Criterion> getCriteria() {
    return criteria.stream()
        .filter(criterion -> !criterion.isDeleted())
        .collect(Collectors.toList());
  }
}
