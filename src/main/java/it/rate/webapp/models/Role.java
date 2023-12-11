package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @EmbeddedId
    private RoleId id;
    private RoleType role;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("interestId")
    private Interest interest;

    private enum RoleType {
        USER, ADMIN
    }
}
