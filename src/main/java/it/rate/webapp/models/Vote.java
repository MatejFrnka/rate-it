package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "votes")
public class Vote {
    @EmbeddedId
    private VoteId id;
    private int voteValue;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("interestId")
    private Interest interest;

    public Vote(User user, Interest interest, int voteValue) {
        this.id = new VoteId(user.getId(), interest.getId());
        this.user = user;
        this.interest = interest;
        this.voteValue = voteValue;
    }
}
