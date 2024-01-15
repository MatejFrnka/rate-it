package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import it.rate.webapp.BaseTest;
import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.exceptions.badrequest.InvalidRatingException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.RatingRepository;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class RatingServiceTest extends BaseTest {
  @MockBean RatingRepository ratingRepository;
  @MockBean CriterionRepository criterionRepository;
  @Autowired RatingService ratingService;

  @Test
  void updateRatingMissingCriterionInRatings() {
    AppUser appUser = new AppUser();
    Place p = new Place();
    Interest i = new Interest();

    p.setInterest(i);

    Criterion c1 = Criterion.builder().id(1L).build();
    Criterion c2 = Criterion.builder().id(2L).build();
    Criterion c3 = Criterion.builder().id(3L).build();

    i.setCriteria(Arrays.asList(c1, c2, c3));

    Rating r1 = new Rating(appUser, p, c1, 5);
    Rating r2 = new Rating(appUser, p, c2, 8);

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
        InvalidRatingException.class, () -> ratingService.updateRating(ratingsDTO, p, appUser));
  }

  @Test
  void updateRatingScoreOutOfRange() {
    AppUser appUser = new AppUser();
    Place p = new Place();
    Interest i = new Interest();

    p.setInterest(i);

    Criterion c1 = Criterion.builder().id(1L).build();
    Criterion c2 = Criterion.builder().id(2L).build();
    Criterion c3 = Criterion.builder().id(3L).build();

    i.setCriteria(Arrays.asList(c1, c2, c3));

    Rating r1 = new Rating(appUser, p, c1, 5);
    Rating r2 = new Rating(appUser, p, c2, 8);

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
        InvalidRatingException.class, () -> ratingService.updateRating(ratingsDTO, p, appUser));
  }

  @Test
  void updateRatingOk() {
    AppUser appUser = new AppUser();
    Place p = new Place();
    Interest i = new Interest();

    p.setInterest(i);

    Criterion c1 = Criterion.builder().id(1L).build();
    Criterion c2 = Criterion.builder().id(2L).build();
    Criterion c3 = Criterion.builder().id(3L).build();

    i.setCriteria(Arrays.asList(c1, c2, c3));

    Rating r1 = new Rating(appUser, p, c1, 5);
    Rating r2 = new Rating(appUser, p, c2, 8);

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

    ratingService.updateRating(ratingsDTO, p, appUser);

    verify(ratingRepository, times(2)).save(any());
    verify(criterionRepository, times(3)).findById(any());
    verify(ratingRepository, times(3)).findById(any());
  }
}
