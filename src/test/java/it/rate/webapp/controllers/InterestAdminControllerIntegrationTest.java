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
  void editPageView() {}

  @Test
  void editPage() {}

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
  void removeVoterReturnsForbiddenForVoter() throws Exception {
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
  void removeVoterReturnsForbiddenForApplicant() throws Exception {
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
  void removeVoterReturnsForbiddenForNoRolesUser() throws Exception {
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
  void removeVoterReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
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
  void removeVoterReturnsRedirectForCreator() throws Exception {
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
  void removeVoterReturnsRedirectForAdmin() throws Exception {
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
  void rejectApplicantReturnsForbiddenForVoter() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_APPLICANT_1"})
  void rejectApplicantReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void rejectApplicantReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_2"})
  void rejectApplicantReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
  void rejectApplicantReturnsRedirectForCreator() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    verify(manageInterestService, times(1)).removeRole(interestId, userId);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void rejectApplicantReturnsRedirectForAdmin() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(delete("/interests/" + interestId + "/admin/users/applicants/" + userId))
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
        .perform(put("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_APPLICANT_1"})
  void acceptApplicantReturnsForbiddenForApplicant() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER"})
  void acceptApplicantReturnsForbiddenForNoRolesUser() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_2"})
  void acceptApplicantReturnsForbiddenForCreatorOfDifferentInterest() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(manageInterestService, times(0)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"USER", "ROLE_CREATOR_1"})
  void acceptApplicantReturnsRedirectForCreator() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    verify(manageInterestService, times(1)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }

  @Test
  @WithMockUser(
      username = "alfonz@alfonz.cz",
      authorities = {"ADMIN"})
  void acceptApplicantReturnsRedirectForAdmin() throws Exception {
    Long interestId = 1L;
    Long userId = 1L;
    mockMvc
        .perform(put("/interests/" + interestId + "/admin/users/applicants/" + userId))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    verify(manageInterestService, times(1)).adjustRole(interestId, userId, Role.RoleType.VOTER);
  }
}
