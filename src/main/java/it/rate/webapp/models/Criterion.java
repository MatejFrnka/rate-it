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
@Table(name = "criteria")
public class Criterion {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    private Interest interest;

    @OneToMany(mappedBy = "criterion")
    private List<Rating> ratings = new ArrayList<>();
}
