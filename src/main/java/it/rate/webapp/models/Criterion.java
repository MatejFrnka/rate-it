package it.rate.webapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
