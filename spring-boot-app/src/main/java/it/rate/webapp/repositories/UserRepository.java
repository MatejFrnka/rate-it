package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
  Optional<AppUser> findByEmail(String email);

  boolean existsByEmailIgnoreCase(String email);

  boolean existsByUsernameIgnoreCase(String username);

  AppUser getByEmail(String email);

  Optional<AppUser> findByUsernameIgnoreCase(String username);

  Optional<AppUser> findByEmailIgnoreCase(String email);

  // find all distinct users who either reviewed or rated a place
  @Query(
          "SELECT DISTINCT a FROM AppUser a "
                  + "LEFT JOIN a.reviews revs "
                  + "LEFT JOIN a.ratings rats "
                  + "WHERE revs.place = :place OR rats.place = :place")
  List<AppUser> findAllDistinctByReviews_PlaceOrRatings_Place(Place place);
}
