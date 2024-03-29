package it.rate.webapp;

import it.rate.webapp.config.ServerRole;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@Transactional
@Rollback
public class BaseIntegrationTest extends BaseTest {

  @Autowired private CriterionRepository criterionRepository;
  @Autowired private InterestRepository interestRepository;
  @Autowired private PlaceRepository placeRepository;
  @Autowired private RatingRepository ratingRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private LikeRepository likeRepository;
  @Autowired private CategoryRepository categoryRepository;

  @BeforeAll
  void setupDatabase() {
    AppUser u1 =
        AppUser.builder()
            .username("Lojza")
            .email("lojza@lojza.cz")
            .password("$2a$10$EBJUECPu/pDHhnt9TX3xmOVHVdYlYdAdR997ilX42EzakA/tL6aQC")
            .serverRole(ServerRole.USER)
            .build();

    AppUser u2 =
        AppUser.builder()
            .username("Alfonz")
            .email("alfonz@alfonz.cz")
            .password("$2a$10$EBJUECPu/pDHhnt9TX3xmOVHVdYlYdAdR997ilX42EzakA/tL6aQC")
            .serverRole(ServerRole.USER)
            .build();

    AppUser u3 =
        AppUser.builder()
            .username("Karel")
            .email("karel@karel.cz")
            .password("$2a$10$EBJUECPu/pDHhnt9TX3xmOVHVdYlYdAdR997ilX42EzakA/tL6aQC")
            .serverRole(ServerRole.ADMIN)
            .build();

    AppUser u4 =
        AppUser.builder()
            .username("Franta")
            .email("franta@franta.cz")
            .password("$2a$10$EBJUECPu/pDHhnt9TX3xmOVHVdYlYdAdR997ilX42EzakA/tL6aQC")
            .serverRole(ServerRole.USER)
            .build();

    AppUser u5 =
        AppUser.builder()
            .username("Hynek")
            .email("hynek@hynek.cz")
            .password("$2a$10$EBJUECPu/pDHhnt9TX3xmOVHVdYlYdAdR997ilX42EzakA/tL6aQC")
            .serverRole(ServerRole.USER)
            .build();
    userRepository.saveAll(List.of(u1, u2, u3, u4, u5));

    List<String> categoryNames =
        Arrays.asList(
            "Food",
            "Drink",
            "Outdoor",
            "Entertainment",
            "Sport",
            "Art & Culture",
            "Relax",
            "Services",
            "Educational");
    List<Category> categories = new ArrayList<>();
    categoryNames.forEach(
        name -> {
          categories.add(new Category(name));
        });
    categoryRepository.saveAll(categories);

    Interest i1 =
        Interest.builder()
            .name("Makove kolacky")
            .description("Makove kolacky jako od babicky")
            .categories(List.of(categories.get(0)))
            .exclusive(true)
            .build();

    Interest i2 =
        Interest.builder()
            .name("Quiet spots")
            .description("Vyjimecne klidna mista")
            .categories(List.of(categories.get(2), categories.get(6)))
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
