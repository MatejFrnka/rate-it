package it.rate.webapp.models;

import it.rate.webapp.config.Constraints;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {
  public Category(String name) {
    this.name = name;
  }

  @Id @GeneratedValue @NotNull private Long id;

  @NotBlank
  @Length(max = Constraints.MAX_NAME_LENGTH)
  private String name;

  @ManyToMany(mappedBy = "categories")
  private List<Interest> interests = new ArrayList<>();
}
