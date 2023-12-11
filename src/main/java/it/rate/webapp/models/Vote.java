package it.rate.webapp.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "votes")
public class Vote {

    @EmbeddedId
    private VoteId id;
    private int value;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("interestId")
    private Interest interest;
}
