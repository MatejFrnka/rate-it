package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.LikeId;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.LikeRepository;
import it.rate.webapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LikeServiceTest extends BaseTest {

  @MockBean InterestRepository interestRepository;
  @MockBean CriterionRepository criterionRepository;
  @MockBean UserRepository userRepository;
  @MockBean LikeRepository likeRepository;

  @Autowired LikeService likeService;

  @Test
  void changeLikeValueCreateLike() {
    Interest i = new Interest();
    AppUser u = AppUser.builder().username("Karel").id(1L).build();

    when(userRepository.getReferenceById(any())).thenReturn(u);
    when(interestRepository.getReferenceById(any())).thenReturn(i);

    likeService.changeLike(i.getId(), u.getId(), true);

    verify(likeRepository, times(1)).save(any());
  }

  @Test
  void changeLikeValueDeleteLike() {
    Interest i = Interest.builder().id(1L).build();
    AppUser u = AppUser.builder().username("Karel").id(1L).build();

    when(userRepository.getReferenceById(any())).thenReturn(u);
    when(interestRepository.getReferenceById(any())).thenReturn(i);

    likeService.changeLike(i.getId(), u.getId(), false);

    verify(likeRepository, times(1)).deleteById(new LikeId(u.getId(), i.getId()));
  }
}
