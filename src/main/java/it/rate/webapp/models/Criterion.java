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
@Table(name = "criteria")
public class Criterion {
  @Id @GeneratedValue private Long id;
  private String name;
  @Builder.Default
  private boolean deleted = false;

  @ManyToOne private Interest interest;

  @OneToMany(mappedBy = "criterion", cascade = CascadeType.ALL)
  @Builder.Default
  private List<Rating> ratings = new ArrayList<>();
}
