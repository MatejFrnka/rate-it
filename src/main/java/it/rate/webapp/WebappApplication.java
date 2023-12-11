package it.rate.webapp;

import it.rate.webapp.models.*;
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
        User u1 = User.builder()
                .username("Lojza")
                .email("lozja@lojza.cz")
                .password("password")
                .build();
        userRepository.save(u1);

        User u2 = User.builder()
                .username("Alfonz")
                .email("alfonz@alfonz.cz")
                .password("password")
                .build();
        userRepository.save(u2);

        Interest i1 = Interest.builder()
                .name("Makové koláčky")
                .description("Makové koláčky jako od babičky")
                .build();
        interestRepository.save(i1);

        Role r1 = Role.builder()
                .id(new RoleId(u1.getId(), i1.getId()))
                .user(u1)
                .interest(i1)
                .role(Role.RoleType.ADMIN)
                .build();
        roleRepository.save(r1);

        Role r2 = Role.builder()
                .id(new RoleId(u2.getId(), i1.getId()))
                .user(u2)
                .interest(i1)
                .role(Role.RoleType.USER)
                .build();
        roleRepository.save(r2);

        Vote v1 = new Vote(u1, i1, 1);
        voteRepository.save(v1);

        Vote v2 = new Vote(u2, i1, 1);
        voteRepository.save(v2);
    }
}
