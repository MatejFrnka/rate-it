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
      LikeRepository likeRepository) {
    this.criterionRepository = criterionRepository;
    this.interestRepository = interestRepository;
    this.placeRepository = placeRepository;
    this.ratingRepository = ratingRepository;
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.likeRepository = likeRepository;
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
    roleRepository.saveAll(List.of(r1, r3, r4, r6));

    Like v1 = new Like(u1, i1);
    Like v2 = new Like(u2, i1);
    Like v3 = new Like(u3, i1);
    Like v4 = new Like(u1, i2);
    Like v5 = new Like(u2, i2);
    Like v6 = new Like(u4, i1);
    likeRepository.saveAll(List.of(v1, v2, v3, v4, v5, v6));

    Place p1 =
        Place.builder()
            .name("Koláčové království")
            .latitude(50.061903)
            .longitude(14.437743)
            .description("Příjemné místo k posezení")
            .address("28, Táborská 583, Nusle, 140 00 Praha 4")
            .creator(u1)
            .interest(i1)
            .build();

    Place p2 =
        Place.builder()
            .name("Matějovo pekařství")
            .latitude(49.200842)
            .longitude(16.612979)
            .description("Top")
            .address("15, M. Horákové 1957, Černá Pole, 602 00 Brno-střed")
            .creator(u2)
            .interest(i1)
            .build();

    Place p3 =
        Place.builder()
            .name("Lavička v parku Ostrava")
            .latitude(49.84983)
            .longitude(18.29078)
            .description("Klídek")
            .address("Komenského Sady, 702 00 Moravská Ostrava a Přívoz")
            .creator(u3)
            .interest(i2)
            .build();

    Place p4 =
            Place.builder()
                    .name("Pekařství Pana Koláčka")
                    .latitude(50.130587)
                    .longitude(14.505279)
                    .description("Super koláčky")
                    .address("Kytlická 756, 190 00 Praha 9-Prosek")
                    .creator(u2)
                    .interest(i1)
                    .build();

    Place p5 =
            Place.builder()
                    .name("Merhautovo pekařství")
                    .latitude(50.425784)
                    .longitude(14.909763)
                    .description("Super pekařství")
                    .address("U Stadionu 1231, 293 01 Mladá Boleslav II")
                    .creator(u2)
                    .interest(i1)
                    .build();

    placeRepository.saveAll(List.of(p1, p2, p3, p4, p5));

    Criterion c1 = Criterion.builder().name("Křupavost").interest(i1).build();

    Criterion c2 = Criterion.builder().name("Velikost").interest(i1).build();

    Criterion c3 = Criterion.builder().name("Zastínění").interest(i2).build();

    Criterion c4 = Criterion.builder().name("Množství tvarohu").interest(i1).build();

    criterionRepository.saveAll(List.of(c1, c2, c3, c4));

    // Koláčky
    Rating rat1 = new Rating(u1, p1, c1, 5);
    Rating rat2 = new Rating(u1, p1, c2, 6);
    Rating rat3 = new Rating(u2, p2, c1, 4);
    Rating rat4 = new Rating(u2, p2, c2, 7);
    Rating rat5 = new Rating(u3, p1, c1, 1);
    Rating rat9 = new Rating(u1, p1, c4, 2);
    Rating rat10 = new Rating(u1, p2, c4, 10);
    Rating rat11 = new Rating(u3, p1, c4, 6);
    Rating rat12 = new Rating(u3, p4, c1, 8);
    Rating rat13 = new Rating(u3, p4, c2, 7);
    Rating rat14 = new Rating(u1, p4, c4, 10);
    Rating rat15 = new Rating(u1, p4, c4, 5);
    Rating rat16 = new Rating(u1, p5, c4, 7);
    Rating rat17 = new Rating(u1, p5, c4, 8);

    // Quiet spots
    Rating rat6 = new Rating(u1, p3, c3, 10);
    Rating rat7 = new Rating(u2, p3, c3, 8);
    Rating rat8 = new Rating(u3, p3, c3, 9);



    ratingRepository.saveAll(List.of(rat1, rat2, rat3, rat4, rat5, rat6, rat7, rat8, rat9, rat10,
            rat11, rat12, rat13, rat14, rat15, rat16, rat17));
  }
}
