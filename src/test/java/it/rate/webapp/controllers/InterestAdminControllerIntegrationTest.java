package it.rate.webapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.rate.webapp.BaseIntegrationTest;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.InterestRepository;

import it.rate.webapp.services.ManageInterestService;
import it.rate.webapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class InterestAdminControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private InterestRepository interestRepository;
  @MockBean private ManageInterestService manageInterestService;
  @MockBean private UserService userService;

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_VOTER_1"})
  void editInterestPageReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_APPLICANT_1"})
  void editInterestPageReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void editInterestPageReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_2"})
  void editInterestPageReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
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
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
  void editInterestPageReturnsSuccessStatusForCreator() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void editInterestPageReturnsSuccessStatusForAdmin() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/edit"))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_VOTER_1"})
  void editUsersReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(userService, times(0)).getByEmail(any());
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_APPLICANT_1"})
  void editUsersReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(userService, times(0)).getByEmail(any());
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void editUsersReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(userService, times(0)).getByEmail(any());
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_2"})
  void editUsersReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(userService, times(0)).getByEmail(any());
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void editUsersReturnsErrorForNonExistentInterest() throws Exception {
    Long interestId = Long.MAX_VALUE;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isNotFound())
        .andReturn();

    verify(userService, times(0)).getByEmail(any());
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
  void editUsersReturnsSuccessStatusForCreator() throws Exception {
    Long interestId = 1L;
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isOk())
        .andReturn();

    verify(userService, times(1)).getByEmail(email);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void editUsersReturnsSuccessForAdmin() throws Exception {
    Long interestId = 1L;
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isOk())
        .andReturn();

    verify(userService, times(1)).getByEmail(email);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
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
                    .param("criteriaNames", criterion1))
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
      authorities = {"USER", "ROLE_VOTER_1", "ROLE_CREATOR_2"})
  void editInterestReturnsForbiddenForInterestVoterAndCreatorOfDifferentInterest()
      throws Exception {
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
                    .param("criteriaNames", criterion1))
            .andExpect(status().isForbidden())
            .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void editInterestReturnsNotFoundForNonExistentInterest() throws Exception {
    Long interestId = Long.MAX_VALUE;
    String interestName = "Zkusit Shushn";
    String interestDescription = "Very gud vetr";
    String criterion1 = "Test criterion";

    MvcResult res =
        mockMvc
            .perform(
                put("/interests/" + interestId + "/admin/edit")
                    .param("name", interestName)
                    .param("description", interestDescription)
                    .param("criteriaNames", criterion1))
            .andExpect(status().isNotFound())
            .andReturn();
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
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
                    .param("criteriaNames", criterion1))
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
      authorities = {"USER", "ROLE_VOTER_1"})
  void removeUserReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_APPLICANT_1"})
  void removeUserReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void removeUserReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_2"})
  void removeUserReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
  void removeUserReturnsRedirectForCreator() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    verify(manageInterestService, times(1)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void removeUserReturnsRedirectForAdmin() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    verify(manageInterestService, times(1)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_VOTER_1"})
  void acceptApplicantReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_APPLICANT_1"})
  void acceptUserReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void acceptUserReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_2"})
  void acceptUserReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
  void acceptUserReturnsRedirectForCreator() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    verify(manageInterestService, times(1)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void acceptUserReturnsRedirectForAdmin() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/" + userId))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    verify(manageInterestService, times(1)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }
}
