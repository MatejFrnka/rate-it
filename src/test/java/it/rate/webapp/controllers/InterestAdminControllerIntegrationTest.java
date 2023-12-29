package it.rate.webapp.controllers;

import it.rate.webapp.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class InterestAdminControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void editPageView() {
      }

    @Test
    void editPage() {
      }

    @Test
    void editUsersPage() {
      }

    @Test
    void removeVoter() {
      }

    @Test
    void rejectApplicant() {
      }

    @Test
    void acceptApplicant() {
      }
}