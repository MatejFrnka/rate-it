package it.rate.webapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import it.rate.webapp.BaseIntegrationTest;
import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.PlaceRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;

class PlaceControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private PlaceRepository placeRepository;

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void testCreateNewPlaceInvalidInterest() throws Exception {
    Long interestId = Long.MAX_VALUE;
    String name = "Test name";
    String description = "Test description";

    // Perform POST request to create a new place and expect redirection
    mockMvc
        .perform(
            post("/interests/" + interestId + "/places/new")
                .param("name", name)
                .param("description", description))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void testCreateNewPlace() throws Exception {
    Long interestId = 1L;
    String name = "Test name";
    String description = "Test description";
    Double latitude = 50.5;
    Double longitude = 15.2;

    // Perform POST request to create a new place and expect redirection
    MvcResult res =
        mockMvc
            .perform(
                post("/interests/" + interestId + "/places/new")
                    .param("name", name)
                    .param("description", description)
                    .param("latitude", String.valueOf(latitude))
                    .param("longitude", String.valueOf(longitude)))
            .andExpect(status().is3xxRedirection())
            .andReturn();

    // Extract the placeId from the redirection URL
    String[] redirectUrl = res.getResponse().getRedirectedUrl().split("/");
    Long placeId = Long.parseLong(redirectUrl[redirectUrl.length - 1]);

    // Fetch the created place from the repository and verify its properties
    Optional<Place> createdPlace = placeRepository.findById(placeId);
    assertTrue(createdPlace.isPresent()); // Check if the place was successfully created
    assertEquals(name, createdPlace.get().getName()); // Verify the name
    assertEquals(description, createdPlace.get().getDescription()); // Verify the description
  }
}
