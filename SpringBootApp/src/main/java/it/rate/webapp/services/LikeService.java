package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Like;
import it.rate.webapp.models.LikeId;
import it.rate.webapp.repositories.LikeRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;

  public boolean existsById(LikeId likeId) {
    return likeRepository.existsById(likeId);
  }

  public void save(@Valid AppUser user, @Valid Interest interest) {
    likeRepository.save(new Like(user, interest));
  }

  public void deleteById(LikeId likeId) {
    likeRepository.deleteById(likeId);
  }

  public void setLike(@Valid AppUser user, @Valid Interest interest, boolean like) {
    if (like) {
      this.save(user, interest);
    } else {
      this.deleteById(new LikeId(user.getId(), interest.getId()));
    }
  }
}
