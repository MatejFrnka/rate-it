package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Like;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.LikeRepository;
import it.rate.webapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LikeServiceTest extends BaseTest {

  @MockBean InterestRepository interestRepository;
  @MockBean CriterionRepository criterionRepository;
  @MockBean UserRepository userRepository;
  @MockBean LikeRepository likeRepository;

  @Autowired LikeService likeService;

  @Test
  void likedInterest() {
    Interest i = new Interest();
    AppUser u = AppUser.builder().username("Karel").id(1L).build();
    Like like = new Like(u, i);
    Authentication auth = new UsernamePasswordAuthenticationToken("Karel", null);
    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(auth);

    when(userRepository.getByEmail(any())).thenReturn(u);
    when(interestRepository.getReferenceById(any())).thenReturn(i);

    likeService.changeLikeValue(i.getId(), "like");

    verify(likeRepository, times(1)).save(any());
  }
}
