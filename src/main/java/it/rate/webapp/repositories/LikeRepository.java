package it.rate.webapp.repositories;

import it.rate.webapp.models.Like;
import it.rate.webapp.models.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {}
