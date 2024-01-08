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

  public void changeLike(Long interestId, Long userId, boolean like) {
    if (like) {
      this.createLike(interestId, userId);
    } else {
      this.deleteById(new LikeId(interestId, userId));
    }
  }

  public void createLike(Long interestId, Long userId) {
    likeRepository.save(
        new Like(
            userRepository.getReferenceById(userId),
            interestRepository.getReferenceById(interestId)));
  }

  public void deleteById(LikeId likeId) {
    likeRepository.deleteById(likeId);
  }

  public boolean existsById(LikeId likeId) {
    return likeRepository.existsById(likeId);
  }
}
