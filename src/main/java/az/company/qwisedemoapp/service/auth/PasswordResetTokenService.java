package az.company.qwisedemoapp.service.auth;

import az.company.qwisedemoapp.domain.entity.PasswordResetToken;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.PasswordResetTokenRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.InvalidInputException;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.exception.TokenExpiredException;
import az.company.qwisedemoapp.model.request.EmailRequest;
import az.company.qwisedemoapp.model.request.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void forgotPassword(EmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(1))
                .build();
        tokenRepository.save(passwordResetToken);
        emailService.sendEmail(request.getEmail(), "Please do not share this token!: ", token);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Passwords do not match");
        }

        PasswordResetToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());
        tokenRepository.delete(token);
    }
}
