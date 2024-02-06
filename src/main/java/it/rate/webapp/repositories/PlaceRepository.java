package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

  // find all distinct places that have been reviewed or rated by a user in given interest
  @Query(
          "SELECT DISTINCT p FROM Place p "
                  + "LEFT JOIN p.reviews revs "
                  + "LEFT JOIN p.ratings rats "
                  + "LEFT JOIN p.interest i "
                  + "WHERE revs.appUser = :appUser AND i = :interest "
                  + "   OR rats.appUser = :appUser AND i = :interest")
  List<Place> findAllDistinctByAppUserAndInterest(AppUser appUser, Interest interest);
}