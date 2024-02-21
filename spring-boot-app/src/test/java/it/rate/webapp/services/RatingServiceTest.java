package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import it.rate.webapp.BaseTest;
import it.rate.webapp.config.ServerRole;
import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.exceptions.badrequest.InvalidRatingException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.RatingRepository;
import java.util.*;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class RatingServiceTest extends BaseTest {
  @MockBean RatingRepository ratingRepository;
  @MockBean CriterionRepository criterionRepository;
  @Autowired RatingService ratingService;
  Interest i1;
  AppUser u1;
  Place p1;
  Criterion c1;
  Criterion c2;
  Criterion c3;
  Rating r1;
  Rating r2;

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

    p1 =
        Place.builder()
            .id(1L)
            .name("Place")
            .description("Description")
            .latitude(1.0)
            .longitude(1.0)
            .build();

    p1.setInterest(i1);

    c1 = Criterion.builder().id(1L).name("Criterion1").build();
    c2 = Criterion.builder().id(2L).name("Criterion2").build();
    c3 = Criterion.builder().id(3L).name("Criterion3").build();

    i1.setCriteria(Arrays.asList(c1, c2, c3));
    r1 = new Rating(u1, p1, c1, 5);
    r2 = new Rating(u1, p1, c2, 8);
  }

  @Test
  void updateRatingMissingCriterionInRatings() {
    Map<Long, Integer> ratings = new HashMap<>();
    ratings.put(1L, null);
    ratings.put(3L, 6);

    RatingsDTO ratingsDTO = new RatingsDTO(ratings);

    when(ratingRepository.findById(eq(r1.getId()))).thenReturn(Optional.of(r1));
    when(ratingRepository.findById(eq(r2.getId()))).thenReturn(Optional.of(r2));

    when(ratingRepository.save(any())).thenAnswer(r -> r.getArgument(0));

    when(criterionRepository.findById(eq(c1.getId()))).thenReturn(Optional.of(c1));
    when(criterionRepository.findById(eq(c2.getId()))).thenReturn(Optional.of(c2));
    when(criterionRepository.findById(eq(c3.getId()))).thenReturn(Optional.of(c3));

    assertThrows(
        InvalidRatingException.class, () -> ratingService.save(ratingsDTO, p1, u1));
  }

  @Test
  void updateRatingScoreOutOfRange() {
    Map<Long, Integer> ratings = new HashMap<>();
    ratings.put(1L, null);
    ratings.put(2L, 6);
    ratings.put(3L, 11);

    RatingsDTO ratingsDTO = new RatingsDTO(ratings);

    when(ratingRepository.findById(eq(r1.getId()))).thenReturn(Optional.of(r1));
    when(ratingRepository.findById(eq(r2.getId()))).thenReturn(Optional.of(r2));

    when(ratingRepository.save(any())).thenAnswer(r -> r.getArgument(0));

    when(criterionRepository.findById(eq(c1.getId()))).thenReturn(Optional.of(c1));
    when(criterionRepository.findById(eq(c2.getId()))).thenReturn(Optional.of(c2));
    when(criterionRepository.findById(eq(c3.getId()))).thenReturn(Optional.of(c3));

    assertThrows(
        ConstraintViolationException.class, () -> ratingService.save(ratingsDTO, p1, u1));
  }

  @Test
  void updateRatingOk() {
    Map<Long, Integer> ratings = new HashMap<>();
    ratings.put(1L, null);
    ratings.put(2L, 6);
    ratings.put(3L, 4);

    RatingsDTO ratingsDTO = new RatingsDTO(ratings);

    when(ratingRepository.findById(eq(r1.getId()))).thenReturn(Optional.of(r1));
    when(ratingRepository.findById(eq(r2.getId()))).thenReturn(Optional.of(r2));

    when(ratingRepository.save(any())).thenAnswer(r -> r.getArgument(0));

    when(criterionRepository.findById(eq(c1.getId()))).thenReturn(Optional.of(c1));
    when(criterionRepository.findById(eq(c2.getId()))).thenReturn(Optional.of(c2));
    when(criterionRepository.findById(eq(c3.getId()))).thenReturn(Optional.of(c3));

    ratingService.save(ratingsDTO, p1, u1);

    verify(ratingRepository, times(2)).save(any());
    verify(criterionRepository, times(3)).findById(any());
    verify(ratingRepository, times(3)).findById(any());
  }
}
