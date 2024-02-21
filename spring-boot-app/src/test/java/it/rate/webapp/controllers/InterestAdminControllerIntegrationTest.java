package it.rate.webapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import it.rate.webapp.BaseIntegrationTest;
import it.rate.webapp.models.Interest;
import it.rate.webapp.repositories.InterestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class InterestAdminControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private InterestRepository interestRepository;

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void editInterestPageReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "franta@franta.cz",
      authorities = {"USER"})
  void editInterestPageReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "hynek@hynek.cz",
      authorities = {"USER"})
  void editInterestPageReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void editInterestPageReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void editInterestPageReturnsErrorForNonExistentInterest() throws Exception {
    Long interestId = Long.MAX_VALUE;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void editInterestPageReturnsSuccessStatusForCreator() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(view().name("interest/form"))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "karel@karel.cz",
      authorities = {"ADMIN"})
  void editInterestPageReturnsSuccessStatusForAdmin() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(view().name("interest/form"))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void editUsersReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "franta@franta.cz",
      authorities = {"USER"})
  void editUsersReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "hynek@hynek.cz",
      authorities = {"USER"})
  void editUsersReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void editUsersReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "karel@karel.cz",
      authorities = {"ADMIN"})
  void editUsersReturnsErrorForNonExistentInterest() throws Exception {
    Long interestId = Long.MAX_VALUE;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void editUsersReturnsSuccessStatusForCreator() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(view().name("interest/users"))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "karel@karel.cz",
      authorities = {"ADMIN"})
  void editUsersReturnsSuccessForAdmin() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(view().name("interest/users"))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void editInterestReturnsRedirectForInterestCreator() throws Exception {
    Long interestId = 1L;
    String interestName = "Zkusit Shushn";
    String interestDescription = "Very gud vetr";
    String criterion1 = "Test criterion";

    MvcResult res =
        mockMvc
            .perform(
                put("/interests/" + interestId + "/admin/edit")
                    .param("name", interestName)
                    .param("description", interestDescription)
                    .param("criteriaNames", criterion1)
                    .param("exclusive", "false"))
            .andExpect(status().is3xxRedirection())
            .andReturn();

    String[] redirectUrl = res.getResponse().getRedirectedUrl().split("/");
    Long newInterestId = Long.parseLong(redirectUrl[redirectUrl.length - 1]);

    Optional<Interest> updatedInterest = interestRepository.findById(newInterestId);
    assertTrue(updatedInterest.isPresent());
    assertEquals(interestName, updatedInterest.get().getName());
    assertEquals(interestDescription, updatedInterest.get().getDescription());
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void editInterestReturnsForbiddenForInterestVoterAndCreatorOfDifferentInterest()
      throws Exception {
    Long interestId = 2L;
    String interestName = "Zkusit Shushn";
    String interestDescription = "Very gud vetr";
    String criterion1 = "Test criterion";

    mockMvc
        .perform(
            put("/interests/" + interestId + "/admin/edit")
                .param("name", interestName)
                .param("description", interestDescription)
                .param("criteriaNames", criterion1)
                .param("exclusive", "false"))
        .andExpect(status().isForbidden())
        .andExpect(view().name("error/page"))
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "karel@karel.cz",
      authorities = {"ADMIN"})
  void editInterestReturnsNotFoundForNonExistentInterest() throws Exception {
    Long interestId = Long.MAX_VALUE;
    String interestName = "Zkusit Shushn";
    String interestDescription = "Very gud vetr";
    String criterion1 = "Test criterion";

    mockMvc
        .perform(
            put("/interests/" + interestId + "/admin/edit")
                .param("name", interestName)
                .param("description", interestDescription)
                .param("criteriaNames", criterion1)
                .param("exclusive", "false"))
        .andExpect(view().name("error/page"))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "karel@karel.cz",
      authorities = {"ADMIN"})
  void editInterestReturnsRedirectForAdmin() throws Exception {
    Long interestId = 1L;
    String interestName = "Zkusit Shushn";
    String interestDescription = "Very gud vetr";
    String criterion1 = "Test criterion";

    MvcResult res =
        mockMvc
            .perform(
                put("/interests/" + interestId + "/admin/edit")
                    .param("name", interestName)
                    .param("description", interestDescription)
                    .param("criteriaNames", criterion1)
                    .param("exclusive", "false"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    String[] redirectUrl = res.getResponse().getRedirectedUrl().split("/");
    Long newInterestId = Long.parseLong(redirectUrl[redirectUrl.length - 1]);

    Optional<Interest> updatedInterest = interestRepository.findById(newInterestId);
    assertTrue(updatedInterest.isPresent());
    assertEquals(interestName, updatedInterest.get().getName());
    assertEquals(interestDescription, updatedInterest.get().getDescription());
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void removeUserReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "franta@franta.cz",
      authorities = {"USER"})
  void removeUserReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "hynek@hynek.cz",
      authorities = {"USER"})
  void removeUserReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void removeUserReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 2L;
    Long userId = 2L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void removeUserReturnsRedirectForCreator() throws Exception {
    Long interestId = 1L;
    Long userId = 3L;

    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(view().name("redirect:/interests/{interestId}/admin/users"))
        .andExpect(status().is3xxRedirection())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "karel@karel.cz",
      authorities = {"ADMIN"})
  void removeUserReturnsRedirectForAdmin() throws Exception {
    Long interestId = 1L;
    Long userId = 2L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(view().name("redirect:/interests/{interestId}/admin/users"))
        .andExpect(status().is3xxRedirection())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void acceptApplicantReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    Long userId = 4L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/invite/" + userId))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "franta@franta.cz",
      authorities = {"USER"})
  void acceptUserReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    Long userId = 4L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/invite/" + userId))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "hynek@hynek.cz",
      authorities = {"USER"})
  void acceptUserReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/invite/" + userId))
        .andExpect(status().isForbidden())
        .andExpect(view().name("error/page"))
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void acceptUserReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 2L;
    Long userId = 2L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/invite/" + userId))
        .andExpect(view().name("error/page"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "lojza@lojza.cz",
      authorities = {"USER"})
  void acceptUserReturnsRedirectForCreator() throws Exception {
    Long interestId = 1L;
    Long userId = 4L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/invite/" + userId))
        .andExpect(view().name("redirect:/interests/{interestId}/admin/invite"))
        .andExpect(status().is3xxRedirection())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "karel@karel.cz",
      authorities = {"ADMIN"})
  void acceptUserReturnsRedirectForAdmin() throws Exception {
    Long interestId = 1L;
    Long userId = 4L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/invite/" + userId))
        .andExpect(view().name("redirect:/interests/{interestId}/admin/invite"))
        .andExpect(status().is3xxRedirection())
        .andReturn();
  }
}
