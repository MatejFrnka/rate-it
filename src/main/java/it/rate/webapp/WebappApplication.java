package it.rate.webapp;

import it.rate.webapp.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class WebappApplication implements CommandLineRunner {
  private CriterionRepository criterionRepository;
  private InterestRepository interestRepository;
  private PlaceRepository placeRepository;
  private RatingRepository ratingRepository;
  private RoleRepository roleRepository;
  private UserRepository userRepository;
  private VoteRepository voteRepository;

  public static void main(String[] args) {
    SpringApplication.run(WebappApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

  }
}
