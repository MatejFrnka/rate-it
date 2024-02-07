package it.rate.webapp.repositories;

import it.rate.webapp.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
  List<Rating> findAllByAppUserAndPlace(AppUser appUser, Place place);

  List<Rating> findAllByCriterionAndPlace(Criterion criterion, Place place);

  List<Rating> findAllByAppUser(AppUser appUser);

  @Query(
      "SELECT DISTINCT r FROM Interest i "
          + "LEFT JOIN i.criteria c "
          + "LEFT JOIN i.places p "
          + "LEFT JOIN c.ratings cr "
          + "LEFT JOIN p.ratings pr "
          + "LEFT JOIN Rating r ON r IN (cr, pr) "
          + "WHERE r.appUser = :appUser AND i = :interest")
  List<Rating> findAllByAppUserAndInterestId(AppUser appUser, Interest interest);

  boolean existsByAppUserAndPlace(AppUser appUser, Place place);
}
