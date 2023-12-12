package it.rate.webapp.repositories;

import it.rate.webapp.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findAllByNameContaining(String query);
}
