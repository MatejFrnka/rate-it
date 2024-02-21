package it.rate.webapp.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.rate.webapp.BaseIntegrationTest;
import it.rate.webapp.dtos.InterestSuggestionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InterestRestControllerTest extends BaseIntegrationTest {

  @Autowired MockMvc mockMvc;

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
                new InterestSuggestionDTO(
                    1L,
                    "Makove kolacky",
                    "Makove kolacky jako od babicky",
                    4L,
                    2L,
                    null,
                    100.75592824812773,
                    List.of(1L)),
                new InterestSuggestionDTO(
                    2L,
                    "Quiet spots",
                    "Vyjimecne klidna mista",
                    2L,
                    1L,
                    null,
                    180.09481290966787,
                    List.of(3L, 7L))));

    // Hradec Králové
    double latitude = 50.209859;
    double longitude = 15.832464;

    mockMvc
        .perform(
            get("/api/v1/interests/suggestions?latitude=" + latitude + "&longitude=" + longitude))
        .andExpect(status().isOk())
        .andExpect(content().string(jsonExpectedResult));
  }

  @Test
  void getAllSuggestionsWithInvalidCoordinatesValueOutOfRange() throws Exception {
    // invalid latitude
    double latitude = 90.43543;
    double longitude = 14.24542;

    mockMvc
        .perform(
            get("/api/v1/interests/suggestions?latitude=" + latitude + "&longitude=" + longitude))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllSuggestionsWithInvalidCoordinatesNull() throws Exception {

    double longitude = 14.24542;
    Double latitude = null;

    mockMvc
        .perform(
            get("/api/v1/interests/suggestions?longitude=" + longitude + "&latitude=" + latitude))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllSuggestionsWithInvalidCoordinatesMissingValue() throws Exception {
    // missing latitude
    double longitude = 14.24542;

    mockMvc
        .perform(get("/api/v1/interests/suggestions?longitude=" + longitude))
        .andExpect(status().isBadRequest());
  }
}
