package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Like;
import it.rate.webapp.models.LikeId;
import it.rate.webapp.repositories.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeService {

  private LikeRepository likeRepository;

  public void changeLike(AppUser user, Interest interest, boolean like) {
    if (like) {
      this.createLike(user, interest);
    } else {
      this.deleteById(new LikeId(user.getId(), interest.getId()));
    }
  }

  public void createLike(AppUser user, Interest interest) {
    likeRepository.save(new Like(user, interest));
  }

  public void deleteById(LikeId likeId) {
    likeRepository.deleteById(likeId);
  }

  public boolean existsById(LikeId likeId) {
    return likeRepository.existsById(likeId);
  }
}
