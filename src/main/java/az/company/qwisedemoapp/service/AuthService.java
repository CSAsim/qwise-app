package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.OtpCode;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.UserPrincipal;
import az.company.qwisedemoapp.domain.repository.RefreshTokenRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.AlreadyExistsException;
import az.company.qwisedemoapp.exception.InvalidInputException;
import az.company.qwisedemoapp.mapper.UserMapper;
import az.company.qwisedemoapp.model.constants.ResponseMessages;
import az.company.qwisedemoapp.model.dto.AuthResponse;
import az.company.qwisedemoapp.model.enums.UserRole;
import az.company.qwisedemoapp.model.enums.UserStatus;
import az.company.qwisedemoapp.model.request.EmailRequest;
import az.company.qwisedemoapp.model.request.LoginUserRequest;
import az.company.qwisedemoapp.model.request.RegisterUserRequest;
import az.company.qwisedemoapp.model.request.ResetPasswordRequest;
import az.company.qwisedemoapp.model.request.VerifyOtpRequest;
import az.company.qwisedemoapp.domain.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final OtpCodeService otpCodeService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    @Transactional
    public AuthResponse login(LoginUserRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        return generateTokens(user);
    }

    @Transactional
    public String logout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUser(email);
        refreshTokenRepository.deleteRefreshTokenByUserId(user.getId());
        return ResponseMessages.LOG_OUT;
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        RefreshToken existing = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidInputException("Invalid refresh token"));

        if (existing.isExpired()) {
            refreshTokenRepository.delete(existing);
            throw new InvalidInputException("Refresh token expired");
        }

        User user = existing.getUser();
        refreshTokenRepository.deleteRefreshTokenByUserId(user.getId());

        return generateTokens(user);
    }

    @Transactional
    public String register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("The user already exists");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Passwords do not match");
        }

        User entity = userMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRoles(Set.of(UserRole.valueOf(request.getRole())));
        entity.setStatus(UserStatus.PENDING_VERIFICATION);
        User user = userRepository.save(entity);

        sendOtp(user);
        return ResponseMessages.OTP_SENT_MESSAGE;
    }

    @Transactional
    public String verifyOtpCode(VerifyOtpRequest request) {
        otpCodeService.validateOtp(request.getOtpCode());
        User user = getUser(request.getEmail());
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return ResponseMessages.OTP_VERIFIED_MESSAGE;
    }

    @Transactional
    public String forgotPassword(EmailRequest request) {
        User user = getUser(request.getEmail());
        sendOtp(user);
        return ResponseMessages.OTP_SENT_MESSAGE;
    }

    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Passwords do not match");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUser(email);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        refreshTokenRepository.deleteRefreshTokenByUserId(user.getId());
        return ResponseMessages.PASSWORD_RESET_MESSAGE;
    }

    private AuthResponse generateTokens(User user) {
        refreshTokenRepository.deleteRefreshTokenByUserId(user.getId());

        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7)); // 7 gün ömür
        return refreshTokenRepository.save(token);
    }

    private void sendOtp(User user) {
        otpCodeService.changeOtpStatus(user.getId());
        OtpCode otpCode = otpCodeService.createOtpCode(user);
        emailService.sendEmail(user.getEmail(), "Please do not share this message!",
                "This is your OTP code: " + otpCode.getCode());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidInputException("Email is wrong"));
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object object = authentication.getPrincipal();
            if (object instanceof UserPrincipal) {
                return ((UserPrincipal) object).getId();
            }
        }
        throw new IllegalStateException("No authenticated user found!");
    }
}