package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Like;
import it.rate.webapp.models.LikeId;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.LikeRepository;
import it.rate.webapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeService {

  private LikeRepository likeRepository;
  private UserRepository userRepository;
  private InterestRepository interestRepository;

  public boolean isLiked(Long userId, Long interestId) {
    return likeRepository.existsById(new LikeId(userId, interestId));
  }

  public void changeLikeValue(Long interestId, String vote) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AppUser currentUser = userRepository.getByEmail(authentication.getName());

    if (vote.equals("like")) {
      likeRepository.save(new Like(currentUser, interestRepository.getReferenceById(interestId)));
    } else {
      likeRepository.deleteById(new LikeId(currentUser.getId(), interestId));
    }
  }
}
