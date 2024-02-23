package it.rate.webapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
  @EmbeddedId @NotNull private RoleId id;

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RoleType roleType;

  @ManyToOne
  @MapsId("userId")
  private AppUser appUser;

  @ManyToOne
  @MapsId("interestId")
  private Interest interest;

  public Role(AppUser appUser, Interest interest, RoleType roleType) {
    this.id = new RoleId(appUser.getId(), interest.getId());
    this.appUser = appUser;
    this.interest = interest;
    this.roleType = roleType;
  }

  public enum RoleType {
    CREATOR,
    VOTER,
    APPLICANT
  }
}
