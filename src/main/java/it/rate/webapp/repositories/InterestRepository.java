package it.rate.webapp.repositories;

import it.rate.webapp.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {


    @Query("SELECT i, COALESCE(SUM(v.voteValue), 0) AS totalValue " +
            "FROM Interest i " +
            "LEFT JOIN i.votes v " +
            "GROUP BY i.id, i.name " +
            "ORDER BY totalValue DESC")
    List<Interest> findAllSortByVoteValueDesc();

    @Query("SELECT i, COALESCE(SUM(v.voteValue), 0) AS totalValue " +
            "FROM Interest i " +
            "LEFT JOIN i.votes v " +
            "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :substring, '%')) " +
            "GROUP BY i.id, i.name " +
            "ORDER BY totalValue DESC")
    List<Interest> findAllByNameSortByVoteValueDesc(@Param("substring") String substring);


}
