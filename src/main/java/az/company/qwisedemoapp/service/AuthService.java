package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.OtpCode;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.RefreshTokenRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.AlreadyExistsException;
import az.company.qwisedemoapp.exception.InvalidInputException;
import az.company.qwisedemoapp.mapper.UserMapper;
import az.company.qwisedemoapp.model.dto.AuthResponse;
import az.company.qwisedemoapp.model.enums.UserRole;
import az.company.qwisedemoapp.model.enums.UserStatus;
import az.company.qwisedemoapp.model.request.ForgotPasswordRequest;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
        AuthResponse authResponse = refresh(user);
        log.info("User {} logged in successfully", user.getEmail());
        return authResponse;
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
        entity.setRoles(Set.of(UserRole.ROLE_STUDENT));
        entity.setStatus(UserStatus.PENDING_VERIFICATION);
        User user = userRepository.save(entity);

        OtpCode otpCode = otpCodeService.createOtpCode(user);

        emailService.sendEmail(user.getEmail(), "Please do not share this message!", "This is your OTP code: " + otpCode.getCode());
        log.info("OTP code has been sent");

        return "Otp code sent your email";
    }

    @Transactional
    public String verifyOtpCode(VerifyOtpRequest request) {
        otpCodeService.validateOtp(request.getOtpCode());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidInputException("Email is wrong"));
        user.setStatus(UserStatus.ACTIVE);
        log.info("Before save - User status: {}", user.getStatus());
        userRepository.save(user);
        log.info("After save - User status: {}", user.getStatus());
        log.info("OTP code has been verified");
        return "Otp code verified successfully";
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidInputException("Email is wrong"));
        OtpCode otpCode = otpCodeService.createOtpCode(user);
        emailService.sendEmail(request.getEmail(), "Please do not share this message!",
                "This is your OTP code: " + otpCode.getCode());
        return "Otp code sent your email";
    }

    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Passwords do not match");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidInputException("Email is wrong"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        refreshTokenRepository.deleteRefreshTokenByUserId(user.getId());
        return "Your password has been reset. Please log in again";
    }

    private AuthResponse refresh(User user) {
        RefreshToken refreshTokenObj = refreshTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> createRefreshToken(user));
        log.info("refresh token is called");
        if (refreshTokenObj.isExpired()) {
            refreshTokenRepository.delete(refreshTokenObj);
            String newRefreshToken = createRefreshToken(user).getToken();
            String accessToken = jwtService.generateToken(user);

            log.info("User {} logged in with new refresh token", user.getEmail());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }

        String newAccessToken = jwtService.generateToken(refreshTokenObj.getUser());
        return new AuthResponse(newAccessToken, refreshTokenObj.getToken());
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        return refreshTokenRepository.save(token);
    }

}
