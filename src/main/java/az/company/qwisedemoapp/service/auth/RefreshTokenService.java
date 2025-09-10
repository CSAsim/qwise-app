package az.company.qwisedemoapp.service.auth;

import az.company.qwisedemoapp.domain.entity.RefreshToken;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.RefreshTokenRepository;
import az.company.qwisedemoapp.exception.InvalidInputException;
import az.company.qwisedemoapp.model.enums.RefreshTokenStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7)); // 7 gün ömür
        token.setStatus(RefreshTokenStatus.ACTIVE);
        return refreshTokenRepository.save(token);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidInputException("Invalid refresh token"));
    }

    @Transactional
    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.findByUserIdAndStatus(userId, RefreshTokenStatus.ACTIVE)
                .ifPresent(token -> {
                    token.setStatus(RefreshTokenStatus.DEACTIVATED);
                    refreshTokenRepository.save(token);
                });

    }

    @Transactional
    public void deleteRefreshTokenByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidInputException("Invalid refresh token"));
        refreshToken.setStatus(RefreshTokenStatus.DEACTIVATED);
        refreshTokenRepository.save(refreshToken);
    }

    public void isTokenExistsByUserId(Long userId) {
        if(!refreshTokenRepository.existsByUserId(userId)) {
            throw new InvalidInputException("User has refresh token");
        }
    }
}
