package it.rate.webapp.repositories;

import it.rate.webapp.models.Vote;
import it.rate.webapp.models.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, VoteId> {
}
