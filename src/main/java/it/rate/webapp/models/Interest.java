package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "interests")
public class Interest {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "interest")
    private List<Place> places = new ArrayList<>();

    @OneToMany(mappedBy = "interest")
    private List<Criterion> criteria = new ArrayList<>();

    @OneToMany(mappedBy = "interest")
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "interest")
    private List<Role> roles = new ArrayList<>();
}
