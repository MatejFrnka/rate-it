package it.rate.webapp;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class WebappApplication implements CommandLineRunner {
  public WebappApplication(
      CriterionRepository criterionRepository,
      InterestRepository interestRepository,
      PlaceRepository placeRepository,
      RatingRepository ratingRepository,
      RoleRepository roleRepository,
      UserRepository userRepository,
      VoteRepository voteRepository) {
    this.criterionRepository = criterionRepository;
    this.interestRepository = interestRepository;
    this.placeRepository = placeRepository;
    this.ratingRepository = ratingRepository;
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.voteRepository = voteRepository;
  }

  private CriterionRepository criterionRepository;
  private InterestRepository interestRepository;
  private PlaceRepository placeRepository;
  private RatingRepository ratingRepository;
  private RoleRepository roleRepository;
  private UserRepository userRepository;
  private LikeRepository likeRepository;

  // check the application.properties file for app.preload-data, default value is true
  @Value("${app.preload-data:true}")
  private boolean preloadData;

  public static void main(String[] args) {
    SpringApplication.run(WebappApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (!preloadData) {
      return;
    }
    AppUser u1 =
        AppUser.builder()
            .username("Lojza")
            .email("lojza@lojza.cz")
            .password("$2a$10$9g1X9rp6meCML3g/h32MyeQ369SEh/hQpZb82eqjpvI71xCIdPAlG")
            .serverRole(ServerRole.USER)
            .build();

    AppUser u2 =
        AppUser.builder()
            .username("Alfonz")
            .email("alfonz@alfonz.cz")
            .password("$2a$10$9g1X9rp6meCML3g/h32MyeQ369SEh/hQpZb82eqjpvI71xCIdPAlG")
            .serverRole(ServerRole.USER)
            .build();

    AppUser u3 =
        AppUser.builder()
            .username("Karel")
            .email("karel@karel.cz")
            .password("$2a$10$9g1X9rp6meCML3g/h32MyeQ369SEh/hQpZb82eqjpvI71xCIdPAlG")
            .serverRole(ServerRole.ADMIN)
            .build();

    AppUser u4 =
        AppUser.builder()
            .username("Franta")
            .email("franta@franta.cz")
            .password("$2a$10$9g1X9rp6meCML3g/h32MyeQ369SEh/hQpZb82eqjpvI71xCIdPAlG")
            .serverRole(ServerRole.USER)
            .build();
    userRepository.saveAll(List.of(u1, u2, u3, u4));

    Interest i1 =
        Interest.builder()
            .name("Makové koláčky")
            .description("Makové koláčky jako od babičky")
            .exclusive(true)
            .build();

    Interest i2 =
        Interest.builder()
            .name("Quiet spots")
            .description("Výjimečně klidná místa")
            .exclusive(false)
            .build();
    interestRepository.saveAll(List.of(i1, i2));

    Role r1 = new Role(u1, i1, Role.RoleType.CREATOR);
    Role r2 = new Role(u2, i1, Role.RoleType.VOTER);
    Role r3 = new Role(u3, i2, Role.RoleType.CREATOR);
    Role r4 = new Role(u3, i1, Role.RoleType.VOTER);
    Role r5 = new Role(u1, i2, Role.RoleType.VOTER);
    Role r6 = new Role(u4, i1, Role.RoleType.APPLICANT);
    roleRepository.saveAll(List.of(r1, r2, r3, r4, r5, r6));

    Like v1 = new Like(u1, i1);
    Like v2 = new Like(u2, i1);
    Like v3 = new Like(u3, i1);
    Like v4 = new Like(u1, i2);
    Like v5 = new Like(u2, i2);
    Like v6 = new Like(u4, i1);
    likeRepository.saveAll(List.of(v1, v2, v3, v4, v5, v6));

    Place p1 =
        Place.builder()
            .name("Koláčkárna")
            .latitude(50.777667)
            .longitude(14.431667)
            .description("Příjemné místo k posezení")
            .address("Kdovíkde 13, Kdovíco 8, 666 66")
            .creator(u1)
            .interest(i1)
            .build();

    Place p2 =
        Place.builder()
            .name("Pekařství na rohu")
            .latitude(50.777876)
            .longitude(14.431276)
            .description("Top")
            .address("Ulice 7, Město 2, 222 42")
            .creator(u2)
            .interest(i1)
            .build();

    Place p3 =
        Place.builder()
            .name("Lavička v parku")
            .latitude(50.787536)
            .longitude(14.873876)
            .description("Klídek")
            .address("Pod Drnem 6, Praha 2, 120 00")
            .creator(u3)
            .interest(i2)
            .build();

    placeRepository.saveAll(List.of(p1, p2, p3));

    Criterion c1 = Criterion.builder().name("Křupavost").interest(i1).build();

    Criterion c2 = Criterion.builder().name("Velikost").interest(i1).build();

    Criterion c3 = Criterion.builder().name("Zastínění").interest(i2).build();

    criterionRepository.saveAll(List.of(c1, c2, c3));

    Rating rat1 = new Rating(u1, p1, c1, 5);
    Rating rat2 = new Rating(u1, p1, c2, 6);
    Rating rat3 = new Rating(u2, p2, c1, 4);
    Rating rat4 = new Rating(u2, p2, c2, 7);
    Rating rat5 = new Rating(u3, p1, c1, 1);
    Rating rat6 = new Rating(u1, p3, c3, 10);
    Rating rat7 = new Rating(u2, p3, c3, 8);
    Rating rat8 = new Rating(u3, p3, c3, 9);
    ratingRepository.saveAll(List.of(rat1, rat2, rat3, rat4, rat5, rat6, rat7, rat8));
  }
}
