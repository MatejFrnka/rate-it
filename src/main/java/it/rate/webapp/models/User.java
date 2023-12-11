package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    @OneToMany(mappedBy = "creator")
    private List<Place> createdPlaces;
    @OneToMany(mappedBy = "user")
    private List<Role> role;
}
