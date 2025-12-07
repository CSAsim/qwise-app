package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.Rating;
import az.company.qwisedemoapp.model.enums.RatingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("""
            SELECT ur FROM Rating ur WHERE ur.periodStart = :periodStart AND ur.type = :type
            ORDER BY ur.score DESC
            """)
    Page<Rating> findAllUserRatingsByType(LocalDateTime periodStart, RatingType type, Pageable pageable);

    @Query("SELECT ur FROM Rating ur WHERE ur.user.id = :userId AND ur.status = 'ACTIVE'")
    List<Rating> findByUserId(Long userId);

    Optional<Rating> findByUserIdAndType(Long userId, RatingType type);

    @Query(value = """
            SELECT t.id, t.rank FROM (
                SELECT r.id,
                       RANK() OVER (
                           PARTITION BY r.type, r.period_start
                           ORDER BY r.score DESC
                       ) AS rank
                FROM ratings r
            ) t
            WHERE t.id IN :ratingIds
            """, nativeQuery = true)
    List<Object[]> findRanksForRatingIds(@Param("ratingIds") List<Long> ratingIds);

    @Query("""
                SELECT COUNT(*) + 1 AS user_rank FROM Rating ur
                WHERE ur.type = :type AND ur.periodStart = :periodStart AND
                ur.score > (SELECT ur1.score FROM Rating ur1
                        WHERE ur1.user.id = :userId AND
                              ur1.periodStart = :periodStart AND
                              ur1.type = :type)
            """)
    Long findUserRank(Long userId, RatingType type, LocalDateTime periodStart);

    @Query("""
                SELECT r FROM Rating r
                WHERE r.status = 'ACTIVE'
                  AND r.periodEnd < :now
            """)
    List<Rating> findExpiredActiveRatings(LocalDateTime now);

    @Modifying
    @Query("""
                UPDATE Rating r
                SET r.status = 'DEACTIVATED'
                WHERE r.status = 'ACTIVE'
                  AND r.periodEnd < :now
            """)
    void deactivateExpiredRatings(LocalDateTime now);
}
