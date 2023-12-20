package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.rate.webapp.BaseTest;
import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.PlaceRepository;
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
    assertThrows(BadRequestException.class, () -> placeService.saveNewPlace(place, interestId));
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
    Place res = placeService.saveNewPlace(place, interestId);

    // Assertions to verify the results
    assertSame(res.getCreator(), creator);
    assertSame(res.getInterest(), interest);

    // Verify that placeRepository.save() was called exactly once with the same Place object used in
    // the test
    verify(placeRepository, times(1)).save(same(place));
  }

  private static Place getPlaceNoId() {
    Place place = new Place();
    place.setName("name");
    place.setDescription("description");
    return place;
  }
}
