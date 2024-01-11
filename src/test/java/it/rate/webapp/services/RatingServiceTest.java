package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.rate.webapp.BaseTest;
import it.rate.webapp.dtos.CriteriaOfPlaceDTO;
import it.rate.webapp.dtos.CriterionAvgRatingDTO;
import it.rate.webapp.dtos.PlaceInfoDTO;
import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RatingRepository;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

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

    Rating r1 = new Rating(appUser, p, c1, 4);
    Rating r2 = new Rating(appUser, p, c2, 5);
    Rating r3 = new Rating(appUser, p, c3, 6);

    Map<Long, Integer> ratings = Map.of(2L, 5, 3L, 6);

    RatingsDTO ratingsDTO = new RatingsDTO(ratings);

    when(ratingRepository.findById(eq(r1.getId()))).thenReturn(Optional.of(r1));
    when(ratingRepository.findById(eq(r2.getId()))).thenReturn(Optional.of(r2));
    when(ratingRepository.findById(eq(r3.getId()))).thenReturn(Optional.of(r3));

    when(ratingRepository.save(any())).thenAnswer(r -> r.getArgument(0));

    when(criterionRepository.findById(eq(c1.getId()))).thenReturn(Optional.of(c1));
    when(criterionRepository.findById(eq(c2.getId()))).thenReturn(Optional.of(c2));
    when(criterionRepository.findById(eq(c3.getId()))).thenReturn(Optional.of(c3));

    assertThrows(
        ResponseStatusException.class, () -> ratingService.updateRating(ratingsDTO, p, appUser));
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

    Rating r1 = new Rating(appUser, p, c1, 4);
    Rating r2 = new Rating(appUser, p, c2, 4);
    Rating r3 = new Rating(appUser, p, c3, 11);

    Map<Long, Integer> ratings = Map.of(1L,4, 2L, 5, 3L, 6);

    RatingsDTO ratingsDTO = new RatingsDTO(ratings);

    when(ratingRepository.findById(eq(r1.getId()))).thenReturn(Optional.of(r1));
    when(ratingRepository.findById(eq(r2.getId()))).thenReturn(Optional.of(r2));
    when(ratingRepository.findById(eq(r3.getId()))).thenReturn(Optional.of(r3));

    when(ratingRepository.save(any())).thenAnswer(r -> r.getArgument(0));

    when(criterionRepository.findById(eq(c1.getId()))).thenReturn(Optional.of(c1));
    when(criterionRepository.findById(eq(c2.getId()))).thenReturn(Optional.of(c2));
    when(criterionRepository.findById(eq(c3.getId()))).thenReturn(Optional.of(c3));

    assertThrows(
        ResponseStatusException.class, () -> ratingService.updateRating(ratingsDTO, p, appUser));
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

    Rating r1 = new Rating(appUser, p, c1, 4);
    Rating r2 = new Rating(appUser, p, c2, 5);
    Rating r3 = new Rating(appUser, p, c3, 6);

    RatingsDTO ratingsDTO = new RatingsDTO(ratings);

    when(ratingRepository.findById(eq(r1.getId()))).thenReturn(Optional.of(r1));
    when(ratingRepository.findById(eq(r2.getId()))).thenReturn(Optional.of(r2));
    when(ratingRepository.findById(eq(r3.getId()))).thenReturn(Optional.of(r3));

    when(ratingRepository.save(any())).thenAnswer(r -> r.getArgument(0));

    when(criterionRepository.findById(eq(c1.getId()))).thenReturn(Optional.of(c1));
    when(criterionRepository.findById(eq(c2.getId()))).thenReturn(Optional.of(c2));
    when(criterionRepository.findById(eq(c3.getId()))).thenReturn(Optional.of(c3));

    verify(ratingService, times(1)).updateRating(ratingsDTO, p, appUser);
  }
}
