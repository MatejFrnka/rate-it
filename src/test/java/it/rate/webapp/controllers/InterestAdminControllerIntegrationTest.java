package it.rate.webapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.rate.webapp.BaseIntegrationTest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.ManageInterestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class InterestAdminControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private ManageInterestService manageInterestService;

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

    verify(manageInterestService, times(0)).getUsersByRole(interestId);
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

    verify(manageInterestService, times(0)).getUsersByRole(interestId);
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

    verify(manageInterestService, times(0)).getUsersByRole(interestId);
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

    verify(manageInterestService, times(0)).getUsersByRole(interestId);
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

    verify(manageInterestService, times(0)).getUsersByRole(interestId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
  void editUsersReturnsSuccessStatusForCreator() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isOk())
        .andReturn();

    verify(manageInterestService, times(1)).getUsersByRole(interestId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void editUsersReturnsSuccessForAdmin() throws Exception {
    Long interestId = 1L;
    mockMvc
        .perform(get("/interests/" + interestId + "/admin/users"))
        .andExpect(status().isOk())
        .andReturn();

    verify(manageInterestService, times(1)).getUsersByRole(interestId);
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
