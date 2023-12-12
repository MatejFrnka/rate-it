package it.rate.webapp;

import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

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
    AppUser u1 = AppUser.builder().username("Lojza").email("lozja@lojza.cz").password("password").build();

    AppUser u2 =
        AppUser.builder().username("Alfonz").email("alfonz@alfonz.cz").password("password").build();

    AppUser u3 = AppUser.builder().username("Karel").email("karel@karel.cz").password("password").build();
    userRepository.saveAll(List.of(u1, u2, u3));

    Interest i1 =
        Interest.builder()
            .name("Makové koláčky")
            .description("Makové koláčky jako od babičky")
            .build();

    Interest i2 =
        Interest.builder().name("Quiet spots").description("Výjimečně klidná místa").build();
    interestRepository.saveAll(List.of(i1, i2));

    Role r1 = new Role(u1, i1, Role.RoleType.ADMIN);
    Role r2 = new Role(u2, i1, Role.RoleType.VOTER);
    Role r3 = new Role(u3, i2, Role.RoleType.ADMIN);
    Role r4 = new Role(u3, i1, Role.RoleType.VOTER);
    Role r5 = new Role(u1, i2, Role.RoleType.VOTER);
    roleRepository.saveAll(List.of(r1, r2, r3, r4, r5));

    Vote v1 = new Vote(u1, i1, 1);
    Vote v2 = new Vote(u2, i1, 1);
    Vote v3 = new Vote(u3, i1, -1);
    Vote v4 = new Vote(u1, i2, 1);
    Vote v5 = new Vote(u2, i2, 1);
    voteRepository.saveAll(List.of(v1, v2, v3, v4, v5));

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
