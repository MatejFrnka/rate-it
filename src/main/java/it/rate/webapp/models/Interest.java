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
@Table(name = "interests")
public class Interest {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "interest")
    private List<Place> places;
}
