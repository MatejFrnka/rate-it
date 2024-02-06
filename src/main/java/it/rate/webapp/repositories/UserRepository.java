package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import it.rate.webapp.models.Review;
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

  @Query(
          "SELECT DISTINCT a FROM AppUser a "
                  + "LEFT JOIN a.reviews revs "
                  + "LEFT JOIN a.ratings rats "
                  + "LEFT JOIN revs.place p_revs "
                  + "LEFT JOIN rats.place p_rats "
                  + "LEFT JOIN Place p ON p IN (p_revs, p_rats) "
                  + "WHERE p = :place")
  List<AppUser> findAllDistinctByReviews_PlaceOrRatings_Place(Place place);
}
