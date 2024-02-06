package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

  @Query(
      "SELECT i, COUNT(l) AS likesCount "
          + "FROM Interest i "
          + "LEFT JOIN i.likes l "
          + "GROUP BY i.id "
          + "ORDER BY likesCount DESC")
  List<Interest> findAllSortByLikes();

  List<Interest> findAllByLikes_AppUser(AppUser appUser);

  // find all distinct interests in which the user has either rated or reviewed
  @Query(
      "SELECT DISTINCT i FROM Interest i "
          + "JOIN i.places p "
          + "LEFT JOIN p.reviews r "
          + "LEFT JOIN p.ratings ra "
          + "WHERE (r.appUser = :appUser OR ra.appUser = :appUser)")
  List<Interest> findAllDistinctByUserRatingsOrReviews(AppUser appUser);
}
