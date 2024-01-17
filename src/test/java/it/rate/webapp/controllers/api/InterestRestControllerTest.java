package it.rate.webapp.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.rate.webapp.BaseIntegrationTest;
import it.rate.webapp.dtos.InterestSuggestionDTO;
import it.rate.webapp.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class InterestRestControllerTest extends BaseIntegrationTest {

  @Autowired MockMvc mockMvc;

  // when RatingRepository is not mocked, the test will fail when running ./gradlew check
  @MockBean private RatingRepository ratingRepository;

  @Test
  void getAllSuggestionsOk() throws Exception {
    mockMvc.perform(get("/api/v1/interests/suggestions")).andExpect(status().isOk());
  }

  @Test
  void getAllSuggestionsWithCoordinatesOk() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonExpectedResult =
        objectMapper.writeValueAsString(
            List.of(
                new InterestSuggestionDTO(1L, "Makove kolacky", 4L, 100.75592824812773, "Fanda"),
                new InterestSuggestionDTO(2L, "Quiet spots", 2L, 180.09481290966787, "Lavicka")));

    mockMvc
        .perform(
            get("/api/v1/interests/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"latitude\": 50.209859, \"longitude\": 15.832464}")) // Hradec Králové
        .andExpect(status().isOk())
        .andExpect(content().string(jsonExpectedResult));
  }

  @Test
  void getAllSuggestionsWithInvalidCoordinatesValueOutOfRange() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/interests/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"latitude\": 90.43543, \"longitude\": 14.24542}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllSuggestionsWithInvalidCoordinatesNull() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/interests/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"latitude\": null, \"longitude\": 14.24542}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllSuggestionsWithInvalidCoordinatesMissingValue() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/interests/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"longitude\": 14.24542}"))
        .andExpect(status().isBadRequest());
  }
}
