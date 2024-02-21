package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.config.ServerRole;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.LikeId;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.LikeRepository;
import it.rate.webapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
  Interest i1;
  AppUser u1;

  @BeforeEach
  void setUp() {
    u1 =
        AppUser.builder()
            .id(1L)
            .username("Lojza")
            .email("lojza@lojza.cz")
            .password("Password1")
            .serverRole(ServerRole.USER)
            .build();
    i1 = Interest.builder().id(1L).name("Interest").description("Description").build();
  }

  @Test
  void changeLikeValueCreateLike() {
    when(userRepository.getReferenceById(any())).thenReturn(u1);
    when(interestRepository.getReferenceById(any())).thenReturn(i1);

    likeService.setLike(u1, i1, true);

    verify(likeRepository, times(1)).save(any());
  }

  @Test
  void changeLikeValueDeleteLike() {
    when(userRepository.getReferenceById(any())).thenReturn(u1);
    when(interestRepository.getReferenceById(any())).thenReturn(i1);

    likeService.setLike(u1, i1, false);

    verify(likeRepository, times(1)).deleteById(new LikeId(u1.getId(), i1.getId()));
  }
}
