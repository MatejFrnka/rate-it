package it.rate.webapp.repositories;

import it.rate.webapp.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {}
