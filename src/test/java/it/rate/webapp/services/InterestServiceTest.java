package it.rate.webapp.services;

import static org.junit.jupiter.api.Assertions.*;

import it.rate.webapp.repositories.InterestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class InterestServiceTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  InterestRepository interestRepository;
  @Test
  void getAllSuggestionDtos() {
    

  }
}
