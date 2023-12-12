package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "votes")
public class Vote {
  @EmbeddedId private VoteId id;
  private int voteValue;

  @ManyToOne
  @MapsId("userId")
  private AppUser appUser;

  @ManyToOne
  @MapsId("interestId")
  private Interest interest;

  public Vote(AppUser appUser, Interest interest, int voteValue) {
    this.id = new VoteId(appUser.getId(), interest.getId());
    this.appUser = appUser;
    this.interest = interest;
    this.voteValue = voteValue;
  }
}
