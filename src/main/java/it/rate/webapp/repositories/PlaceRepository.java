package it.rate.webapp.repositories;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(
            "SELECT DISTINCT p FROM AppUser a "
                    + "LEFT JOIN a.reviews revs "
                    + "LEFT JOIN a.ratings rats "
                    + "LEFT JOIN revs.place p_revs "
                    + "LEFT JOIN rats.place p_rats "
                    + "LEFT JOIN Place p ON p IN (p_revs, p_rats) "
                    + "LEFT JOIN p.interest i "
                    + "WHERE a = :appUser AND i = :interest")
    List<Place> findAllDistinctByAppUserAndInterest(AppUser appUser, Interest interest);
}
