package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.RefreshToken;
import az.company.qwisedemoapp.model.enums.RefreshTokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserIdAndStatus(Long userId, RefreshTokenStatus status);
    Optional<RefreshToken> findByToken(String refreshToken);
    boolean existsByUserId(Long userId);
}
