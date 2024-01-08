package it.rate.webapp.services;

import it.rate.webapp.models.Like;
import it.rate.webapp.models.LikeId;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.LikeRepository;
import it.rate.webapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
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

  public void changeLikeValue(Long interestId, Long userId, int likeValue) {
    if (likeValue == 1) {
      this.createLike(interestId, userId);
    } else {
      this.deleteLike(interestId, userId);
    }
  }

  public void createLike(Long interestId, Long userId) {
    likeRepository.save(
        new Like(
            userRepository.getReferenceById(userId),
            interestRepository.getReferenceById(interestId)));
  }

  public void deleteLike(Long interestId, Long userId) {
    likeRepository.deleteById(new LikeId(userId, interestId));
  }
}
