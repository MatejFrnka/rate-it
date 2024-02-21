package it.rate.webapp.models;

import it.rate.webapp.config.Constraints;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
  @Id @GeneratedValue @NotNull private Long id;

  @NotBlank
  @Length(max = Constraints.MAX_NAME_LENGTH)
  private String name;

  @Builder.Default private boolean deleted = false;

  @ManyToOne private Interest interest;

  @OneToMany(mappedBy = "criterion", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Rating> ratings = new ArrayList<>();
}
