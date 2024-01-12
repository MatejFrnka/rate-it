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
import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RatingRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class PlaceServiceTest extends BaseTest {

  @MockBean Authentication authentication;
  @MockBean SecurityContext securityContext;
  @MockBean InterestService interestService;
  @MockBean UserService userService;
  @MockBean PlaceRepository placeRepository;
  @MockBean RatingRepository ratingRepository;

  @Autowired PlaceService placeService;

  @BeforeEach
  void setupAuthentication() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("joe");

    SecurityContextHolder.setContext(securityContext);
  }

  @Test()
  void saveNewPlaceInvalidInterest() {
    Long interestId = 2L;
    Place place = getPlaceNoId();

    // Mock the userService to return a new AppUser object when findByEmail is called with any
    // string
    when(userService.findByEmail(any())).thenReturn(Optional.of(new AppUser()));

    // Mock the interestService to return an empty Optional when findInterestById is called with the
    // specified interestId
    // This simulates the scenario where no interest is found for the given ID
    when(interestService.findInterestById(eq(interestId))).thenReturn(Optional.empty());

    // Execute the test and verify that a BadRequestException is thrown
    // The assertThrows method checks that the specified exception is thrown when the lambda
    // expression is executed
    assertThrows(BadRequestException.class, () -> placeService.savePlace(place, interestId));
    // The lambda expression calls the saveNewPlace method with the test data, which should throw
    // the exception due to the invalid interest ID
  }

  @Test
  void saveNewPlace() throws BadRequestException {
    // Prepare test data and mock responses
    Long interestId = 2L;
    Place place = getPlaceNoId();
    AppUser creator = new AppUser();
    Interest interest = new Interest();

    // Mock the userService to return the created AppUser when findByEmail is called with any string
    when(userService.findByEmail(any())).thenReturn(Optional.of(creator));

    // Mock the interestService to return the created Interest when findInterestById is called with
    // the specific interestId
    when(interestService.findInterestById(eq(interestId))).thenReturn(Optional.of(interest));

    // Mock the placeRepository to return whatever Place object it receives
    when(placeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    // Call the method under test
    Place res = placeService.savePlace(place, interestId);

    // Assertions to verify the results
    assertSame(res.getCreator(), creator);
    assertSame(res.getInterest(), interest);

    // Verify that placeRepository.save() was called exactly once with the same Place object used in
    // the test
    verify(placeRepository, times(1)).save(same(place));
  }

  @Test
  void loggedUserIsCreator() {
    Long placeId = 2L;
    Place place = getPlaceNoId();
    AppUser creator = new AppUser();
    creator.getCreatedPlaces().add(place);
    place.setCreator(creator);

    when(userService.findByEmail(any())).thenReturn(Optional.of(creator));
    when(placeRepository.findById(eq(placeId))).thenReturn(Optional.of(place));

    assertEquals(place.getCreator(), creator);
  }

  @Test
  void loggedUserIsNotCreator() {
    Long placeId = 2L;
    Place place = getPlaceNoId();
    AppUser creator = new AppUser();

    when(userService.findByEmail(any())).thenReturn(Optional.of(creator));
    when(placeRepository.findById(eq(placeId))).thenReturn(Optional.of(place));

    assertNotEquals(place.getCreator(), creator);
  }

  @Test
  void getPlaceInfoDTOSHappyCase() {
    Place place = getPlaceNoId();
    Interest interest = new Interest();
    AppUser userOne = new AppUser();
    AppUser userTwo = new AppUser();

    List<Criterion> criteria = Arrays.asList(new Criterion(), new Criterion());

    List<Rating> ratings =
        Arrays.asList(
            new Rating(userOne, place, criteria.get(0), 3),
            new Rating(userOne, place, criteria.get(1), 4),
            new Rating(userTwo, place, criteria.get(0), 5),
            new Rating(userTwo, place, criteria.get(1), 6));

    interest.setPlaces(List.of(place));
    place.setRatings(ratings);
    place.setInterest(interest);
    interest.setCriteria(criteria);

    List<PlaceInfoDTO> expectedResult =
        List.of(
            new PlaceInfoDTO(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getAverageRating(),
                new CriterionAvgRatingDTO(criteria.get(1).getId(), criteria.get(1).getName(), 5D),
                new CriterionAvgRatingDTO(criteria.get(0).getId(), criteria.get(0).getName(), 4D)));

    when(ratingRepository.findAllByCriterionAndPlace(criteria.get(0), place))
        .thenReturn(Arrays.asList(ratings.get(0), ratings.get(2)));

    when(ratingRepository.findAllByCriterionAndPlace(criteria.get(1), place))
        .thenReturn(Arrays.asList(ratings.get(1), ratings.get(3)));

    List<PlaceInfoDTO> actualResult = placeService.getPlaceInfoDTOS(interest);

    assertNotNull(actualResult);
    assertEquals(actualResult, expectedResult);
  }

  @Test
  void getPlaceInfoDTOSNoCriteria() {
    Place place = getPlaceNoId();
    Interest interest = new Interest();

    interest.setPlaces(List.of(place));
    place.setInterest(interest);

    assertThrows(IllegalStateException.class, () -> placeService.getPlaceInfoDTOS(interest));
  }

  @Test
  void getCriteriaOfPlaceDtoHappyCase() {
    Place place = getPlaceNoId();
    Interest interest = new Interest();
    AppUser userOne = new AppUser();
    AppUser userTwo = new AppUser();

    List<Criterion> criteria = Arrays.asList(new Criterion(), new Criterion());

    List<Rating> ratings =
        Arrays.asList(
            new Rating(userOne, place, criteria.get(0), 3),
            new Rating(userOne, place, criteria.get(1), 4),
            new Rating(userTwo, place, criteria.get(0), 5),
            new Rating(userTwo, place, criteria.get(1), 6));

    place.setRatings(ratings);
    place.setInterest(interest);
    interest.setCriteria(criteria);

    CriteriaOfPlaceDTO expectedResult =
        new CriteriaOfPlaceDTO(
            Arrays.asList(
                new CriterionAvgRatingDTO(criteria.get(0).getId(), criteria.get(0).getName(), 4D),
                new CriterionAvgRatingDTO(criteria.get(1).getId(), criteria.get(1).getName(), 5D)));

    when(ratingRepository.findAllByCriterionAndPlace(criteria.get(0), place))
        .thenReturn(Arrays.asList(ratings.get(0), ratings.get(2)));

    when(ratingRepository.findAllByCriterionAndPlace(criteria.get(1), place))
        .thenReturn(Arrays.asList(ratings.get(1), ratings.get(3)));

    CriteriaOfPlaceDTO actualResult = placeService.getCriteriaOfPlaceDTO(place);

    assertNotNull(actualResult);
    assertEquals(actualResult, expectedResult);
  }

  @Test
  void getCriteriaOfPlaceDtoNoRatings() {
    Place place = getPlaceNoId();
    Interest interest = new Interest();
    List<Criterion> criteria = List.of(new Criterion());
    place.setInterest(interest);
    interest.setCriteria(criteria);

    when(ratingRepository.findAllByCriterionAndPlace(criteria.get(0), place))
        .thenReturn(new ArrayList<>());

    CriteriaOfPlaceDTO expectedResult =
        new CriteriaOfPlaceDTO(
            List.of(
                new CriterionAvgRatingDTO(criteria.get(0).getId(), criteria.get(0).getName(), -1D)));

    CriteriaOfPlaceDTO actualResult = placeService.getCriteriaOfPlaceDTO(place);

    assertNotNull(actualResult);
    assertEquals(actualResult, expectedResult);
  }

  private static Place getPlaceNoId() {
    Place place = new Place();
    place.setName("name");
    place.setDescription("description");
    return place;
  }
}
