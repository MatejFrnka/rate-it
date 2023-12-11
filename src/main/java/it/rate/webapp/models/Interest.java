package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "interests")
@Getter
@Setter
@NoArgsConstructor
public class Interest {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "interest")
    private List<Place> places;
}
