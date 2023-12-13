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

  @Enumerated(EnumType.STRING)
  private RoleType role;

  @ManyToOne
  @MapsId("userId")
  private AppUser appUser;

  @ManyToOne
  @MapsId("interestId")
  private Interest interest;

  public Role(AppUser appUser, Interest interest, RoleType role) {
    this.id = new RoleId(appUser.getId(), interest.getId());
    this.appUser = appUser;
    this.interest = interest;
    this.role = role;
  }

  public enum RoleType {
    ADMIN,
    VOTER,
    APPLICANT
  }
}
