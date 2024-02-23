package it.rate.webapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like {
  @EmbeddedId @NotNull private LikeId id;

  @ManyToOne
  @MapsId("userId")
  private AppUser appUser;

  @ManyToOne
  @MapsId("interestId")
  private Interest interest;

  public Like(AppUser appUser, Interest interest) {
    this.id = new LikeId(appUser.getId(), interest.getId());
    this.appUser = appUser;
    this.interest = interest;
  }
}
