package it.rate.webapp.repositories;

import it.rate.webapp.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

  @Query(
      "SELECT i, COUNT(l) AS likesCount "
          + "FROM Interest i "
          + "LEFT JOIN i.likes l "
          + "GROUP BY i.id "
          + "ORDER BY likesCount DESC")
  List<Interest> findAllSortByLikes();

  @Query(
      "SELECT i, COUNT(l) AS likesCount "
          + "FROM Interest i "
          + "LEFT JOIN i.likes l "
          + "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :substring, '%')) "
          + "GROUP BY i.id "
          + "ORDER BY likesCount DESC")
  List<Interest> findAllByNameSortByLikes(@Param("substring") String substring);

  List<Interest> findAllByNameContaining(String query);

  List<Interest> findAllByLikes_AppUser_Email(String email);

  List<Interest> findAllByLikes_AppUser_EmailOrderByNameAscLikesDesc(String email);
}
