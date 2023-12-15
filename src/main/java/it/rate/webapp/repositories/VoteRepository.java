package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Like;
import it.rate.webapp.models.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface VoteRepository extends JpaRepository<Like, LikeId> {

    boolean existsByAppUserIdAndInterestId(Long userId, Long interestId);

    @Transactional
    void deleteByAppUserAndInterestId(AppUser user,Long id);
}