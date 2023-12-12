package it.rate.webapp.repositories;

import it.rate.webapp.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {}
