package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
  @EmbeddedId private RoleId id;
  private RoleType role;

  @ManyToOne
  @MapsId("userId")
  private User user;

  @ManyToOne
  @MapsId("interestId")
  private Interest interest;

  public Role(User user, Interest interest, RoleType role) {
    this.id = new RoleId(user.getId(), interest.getId());
    this.user = user;
    this.interest = interest;
    this.role = role;
  }

  public enum RoleType {
    USER,
    ADMIN
  }
}
