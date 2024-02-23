package it.rate.webapp.services;

import it.rate.webapp.BaseTest;
import it.rate.webapp.config.ServerRole;
import it.rate.webapp.dtos.LikedInterestsDTO;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.InterestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class InterestServiceTest extends BaseTest {

  @MockBean InterestRepository interestRepository;

  @Autowired InterestService interestService;

  @Test
  void getLikedInterestsDTOS() {
    // checking alphabetical order
    AppUser u1 =
        AppUser.builder()
            .id(1L)
            .username("Lojza")
            .email("lojza@lojza.cz")
            .password("Password1")
            .serverRole(ServerRole.USER)
            .build();
    Interest i1 = Interest.builder().id(1L).name("zTest").description("desc").build();
    Interest i2 = Interest.builder().id(2L).name("BTest").description("desc").build();
    Interest i3 = Interest.builder().id(3L).name("aTest").description("desc").build();

    when(interestRepository.findAllByLikes_AppUser(u1)).thenReturn(List.of(i1, i2, i3));

    List<LikedInterestsDTO> expected =
        List.of(new LikedInterestsDTO(i3), new LikedInterestsDTO(i2), new LikedInterestsDTO(i1));

    List<LikedInterestsDTO> result = interestService.getLikedInterestsDTOS(u1);

    assertEquals(result, expected);
  }
}
