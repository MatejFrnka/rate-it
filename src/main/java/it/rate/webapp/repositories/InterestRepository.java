package it.rate.webapp.repositories;

import it.rate.webapp.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {


    List<Interest> findAllByNameContains(String query);

}
