package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Modifying
    @Query(value = "DELETE FROM RefreshToken r WHERE r.user.id = :userId")
    void deleteRefreshTokenByUserId(@Param("userId") Long userId);
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByToken(String refreshToken);
}
