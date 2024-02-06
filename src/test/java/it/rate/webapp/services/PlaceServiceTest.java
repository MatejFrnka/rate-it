package it.rate.webapp.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import it.rate.webapp.BaseTest;
import it.rate.webapp.config.ServerRole;
import it.rate.webapp.dtos.*;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RatingRepository;
import java.util.*;
import it.rate.webapp.repositories.ReviewRepository;
import it.rate.webapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class PlaceServiceTest extends BaseTest {

  @MockBean PlaceRepository placeRepository;
  @MockBean RatingRepository ratingRepository;
  @MockBean ReviewRepository reviewRepository;
  @MockBean UserRepository userRepository;

  @Autowired PlaceService placeService;
  Interest i1;
  AppUser u1;
  AppUser u2;
  Place p1;

  Rating r1;
  Rating r2;
  Rating r3;
  Rating r4;
  Review rev1;
  List<Criterion> criteria;
  List<Rating> ratings;
  List<Review> reviews;

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
    u2 =
        AppUser.builder()
            .id(2L)
            .username("Franta")
            .email("franta@franta.cz")
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

    criteria =
        List.of(
            Criterion.builder().id(1L).name("Criterion1").build(),
            Criterion.builder().id(2L).name("Criterion2").build());

    r1 = new Rating(u1, p1, criteria.get(0), 3);
    r2 = new Rating(u1, p1, criteria.get(1), 4);
    r3 = new Rating(u2, p1, criteria.get(0), 5);
    r4 = new Rating(u2, p1, criteria.get(1), 6);

    ratings = List.of(r1, r2, r3, r4);

    rev1 = new Review(u1, p1, "Review1");
    reviews = List.of(rev1);
  }

  @Test
  void saveNewPlace() throws BadRequestException {
    // Mock the placeRepository to return whatever Place object it receives
    when(placeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    PlaceInDTO placeDTO =
        new PlaceInDTO(
            p1.getName(),
            p1.getAddress(),
            p1.getDescription(),
            p1.getLatitude(),
            p1.getLongitude());
    // Call the method under test
    Place res = placeService.save(placeDTO, i1, u1);

    // Assertions to verify the results
    assertSame(res.getCreator(), u1);
    assertSame(res.getInterest(), i1);

    // Verify that placeRepository.save() was called exactly once with the same Place object used in
    // the test
    verify(placeRepository, times(1)).save(same(res));
  }

  @Test
  void getPlaceInfoDTOSHappyCase() {
    CriterionAvgRatingDTO criterionAvgRatingOne =
        new CriterionAvgRatingDTO(criteria.get(1).getId(), criteria.get(1).getName(), 5D);
    CriterionAvgRatingDTO criterionAvgRatingTwo =
        new CriterionAvgRatingDTO(criteria.get(0).getId(), criteria.get(0).getName(), 4D);

    i1.setPlaces(List.of(p1));
    p1.setRatings(ratings);
    p1.setInterest(i1);
    i1.setCriteria(criteria);

    List<PlaceInfoDTO> expectedResult =
        List.of(new PlaceInfoDTO(p1, Set.of(criterionAvgRatingOne, criterionAvgRatingTwo)));

    when(ratingRepository.findAllByCriterionAndPlaceAndCriterionDeletedFalse(criteria.get(0), p1))
        .thenReturn(Arrays.asList(ratings.get(0), ratings.get(2)));

    when(ratingRepository.findAllByCriterionAndPlaceAndCriterionDeletedFalse(criteria.get(1), p1))
        .thenReturn(Arrays.asList(ratings.get(1), ratings.get(3)));

    List<PlaceInfoDTO> actualResult = placeService.getPlaceInfoDTOS(i1);

    assertNotNull(actualResult);
    assertEquals(actualResult, expectedResult);
  }

  @Test
  void getPlaceInfoDTOSNoCriteria() {
    i1.setPlaces(List.of(p1));
    p1.setInterest(i1);

    List<PlaceInfoDTO> expectedResult = List.of(new PlaceInfoDTO(p1, Set.of()));

    assertEquals(expectedResult, placeService.getPlaceInfoDTOS(i1));
  }

  @Test
  void getCriteriaOfPlaceDtoHappyCase() {
    p1.setRatings(ratings);
    p1.setInterest(i1);
    i1.setCriteria(criteria);

    CriteriaOfPlaceDTO expectedResult =
        new CriteriaOfPlaceDTO(
            Arrays.asList(
                new CriterionAvgRatingDTO(criteria.get(0).getId(), criteria.get(0).getName(), 4D),
                new CriterionAvgRatingDTO(criteria.get(1).getId(), criteria.get(1).getName(), 5D)));

    when(ratingRepository.findAllByCriterionAndPlaceAndCriterionDeletedFalse(criteria.get(0), p1))
        .thenReturn(Arrays.asList(ratings.get(0), ratings.get(2)));

    when(ratingRepository.findAllByCriterionAndPlaceAndCriterionDeletedFalse(criteria.get(1), p1))
        .thenReturn(Arrays.asList(ratings.get(1), ratings.get(3)));

    CriteriaOfPlaceDTO actualResult = placeService.getCriteriaOfPlaceDTO(p1);

    assertNotNull(actualResult);
    assertEquals(actualResult, expectedResult);
  }

  @Test
  void getCriteriaOfPlaceDtoNoRatings() {
    List<Criterion> criteria = List.of(Criterion.builder().id(1L).name("Criterion1").build());
    p1.setInterest(i1);
    i1.setCriteria(criteria);

    when(ratingRepository.findAllByCriterionAndPlaceAndCriterionDeletedFalse(criteria.get(0), p1))
        .thenReturn(new ArrayList<>());

    CriteriaOfPlaceDTO expectedResult =
        new CriteriaOfPlaceDTO(
            List.of(
                new CriterionAvgRatingDTO(
                    criteria.get(0).getId(), criteria.get(0).getName(), Double.NaN)));

    CriteriaOfPlaceDTO actualResult = placeService.getCriteriaOfPlaceDTO(p1);

    assertNotNull(actualResult);
    assertEquals(actualResult, expectedResult);
  }

  @Test
  void getPlaceReviewDTOsByPlace() {
    List<PlaceReviewDTO> expectedResult =
        List.of(
            new PlaceReviewDTO(
                u1,
                p1,
                rev1.getText(),
                List.of(new RatingDTO(r1), new RatingDTO(r2)),
                3.5,
                rev1.getCreatedAt()),
            new PlaceReviewDTO(
                u2,
                p1,
                null,
                List.of(new RatingDTO(r3), new RatingDTO(r4)),
                5.5,
                r4.getCreatedAt()));

    when(userRepository.findAllDistinctByReviews_PlaceOrRatings_Place(p1))
        .thenReturn(List.of(u1, u2));
    when(reviewRepository.findById(new ReviewId(p1.getId(), u1.getId())))
        .thenReturn(Optional.of(rev1));
    when(reviewRepository.findById(new ReviewId(p1.getId(), u2.getId())))
        .thenReturn(Optional.empty());
    when(ratingRepository.findAllByAppUserAndPlaceAndCriterionDeletedFalse(u1, p1))
        .thenReturn(List.of(r1, r2));
    when(ratingRepository.findAllByAppUserAndPlaceAndCriterionDeletedFalse(u2, p1))
        .thenReturn(List.of(r3, r4));

    assertThat(placeService.getPlaceReviewDTOs(p1), containsInAnyOrder(expectedResult.toArray()));

    verify(userRepository, times(1))
        .findAllDistinctByReviews_PlaceOrRatings_Place(any(Place.class));
    verify(reviewRepository, times(2)).findById(any(ReviewId.class));
    verify(ratingRepository, times(2))
        .findAllByAppUserAndPlaceAndCriterionDeletedFalse(any(AppUser.class), any(Place.class));
  }

  @Test
  void getPlaceReviewDTOsyByInterestAndUser1() {
    List<PlaceReviewDTO> expectedResult =
        List.of(
            new PlaceReviewDTO(
                u1,
                p1,
                rev1.getText(),
                List.of(new RatingDTO(r1), new RatingDTO(r2)),
                3.5,
                rev1.getCreatedAt()));

    when(placeRepository.findAllDistinctByAppUserAndInterest(u1, i1)).thenReturn(List.of(p1));
    when(reviewRepository.findById(new ReviewId(p1.getId(), u1.getId())))
        .thenReturn(Optional.of(rev1));
    when(ratingRepository.findAllByAppUserAndPlaceAndCriterionDeletedFalse(u1, p1))
        .thenReturn(List.of(r1, r2));

    Comparator<PlaceReviewDTO> comparator =
        Comparator.comparing(PlaceReviewDTO::timestamp).reversed();

    assertThat(
        placeService.getPlaceReviewDTOs(u1, i1, comparator),
        containsInAnyOrder(expectedResult.toArray()));

    verify(placeRepository, times(1))
        .findAllDistinctByAppUserAndInterest(any(AppUser.class), any(Interest.class));
    verify(reviewRepository, times(1)).findById(any(ReviewId.class));
    verify(ratingRepository, times(1))
        .findAllByAppUserAndPlaceAndCriterionDeletedFalse(any(AppUser.class), any(Place.class));
  }

  @Test
  void getPlaceReviewDTOsyByInterestAndUser2() {
    List<PlaceReviewDTO> expectedResult =
        List.of(
            new PlaceReviewDTO(
                u2,
                p1,
                null,
                List.of(new RatingDTO(r3), new RatingDTO(r4)),
                5.5,
                r4.getCreatedAt()));
    when(placeRepository.findAllDistinctByAppUserAndInterest(u2, i1)).thenReturn(List.of(p1));
    when(reviewRepository.findById(new ReviewId(p1.getId(), u2.getId())))
        .thenReturn(Optional.empty());
    when(ratingRepository.findAllByAppUserAndPlaceAndCriterionDeletedFalse(u2, p1))
        .thenReturn(List.of(r3, r4));

    Comparator<PlaceReviewDTO> comparator =
        Comparator.comparing(PlaceReviewDTO::timestamp).reversed();

    assertThat(
        placeService.getPlaceReviewDTOs(u2, i1, comparator),
        containsInAnyOrder(expectedResult.toArray()));

    verify(placeRepository, times(1))
        .findAllDistinctByAppUserAndInterest(any(AppUser.class), any(Interest.class));
    verify(reviewRepository, times(1)).findById(any(ReviewId.class));
    verify(ratingRepository, times(1))
        .findAllByAppUserAndPlaceAndCriterionDeletedFalse(any(AppUser.class), any(Place.class));
  }
}
