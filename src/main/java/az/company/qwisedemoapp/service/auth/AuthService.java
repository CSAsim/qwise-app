package az.company.qwisedemoapp.service.auth;

import az.company.qwisedemoapp.domain.entity.OtpCode;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.UserPrincipal;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.AlreadyExistsException;
import az.company.qwisedemoapp.exception.InvalidInputException;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.exception.TokenExpiredException;
import az.company.qwisedemoapp.mapper.UserMapper;
import az.company.qwisedemoapp.model.constants.ExceptionMessages;
import az.company.qwisedemoapp.model.constants.ResponseMessages;
import az.company.qwisedemoapp.model.dto.response.AuthResponseDto;
import az.company.qwisedemoapp.model.enums.UserRole;
import az.company.qwisedemoapp.model.enums.UserStatus;
import az.company.qwisedemoapp.model.dto.request.ChangePasswordRequestDto;
import az.company.qwisedemoapp.model.dto.request.EmailRequestDto;
import az.company.qwisedemoapp.model.dto.request.LoginUserRequestDto;
import az.company.qwisedemoapp.model.dto.request.RegisterUserRequestDto;
import az.company.qwisedemoapp.model.dto.request.VerifyOtpRequestDto;
import az.company.qwisedemoapp.domain.entity.RefreshToken;
import az.company.qwisedemoapp.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RatingService ratingService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final JwtService jwtService;
    private final OtpCodeService otpCodeService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDto login(LoginUserRequestDto request) {
        log.info("Login user with email {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User" + ExceptionMessages.NOT_FOUND));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidInputException("Password is wrong");
        }
        log.info("Login user with email {} successfully", request.getEmail());
        user.setStatus(UserStatus.ACTIVE);
        return generateNewToken(user);
    }

    @Transactional
    public String logout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUser(email);
        user.setStatus(UserStatus.INACTIVE);
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());
        SecurityContextHolder.clearContext();
        return ResponseMessages.LOG_OUT;
    }

    @Transactional
    public AuthResponseDto refresh(String refreshToken) {
        RefreshToken existing = refreshTokenService.findByToken(refreshToken);

        if (existing.isExpired()) {
            refreshTokenService.deleteRefreshTokenByToken(refreshToken);
            throw new TokenExpiredException("Refresh token expired");
        }

        User user = existing.getUser();
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());

        return generateNewToken(user);
    }

    @Transactional
    public String register(RegisterUserRequestDto request) {
        log.info("Register user with email {}", request.getEmail());
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

        ratingService.createInitialRatingsForUser(user);

        log.info("Register user with email {} successfully", request.getEmail());
        return ResponseMessages.OTP_SENT_MESSAGE;
    }

    @Transactional
    public String verifyOtpCode(VerifyOtpRequestDto request) {
        otpCodeService.validateOtp(request.getOtpCode());
        User user = getUser(request.getEmail());
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return ResponseMessages.OTP_VERIFIED_MESSAGE;
    }

    @Transactional
    public void verifyPasswordResetToken(VerifyOtpRequestDto request) {
        passwordResetTokenService.validateToken(request.getOtpCode());
        User user = getUser(request.getEmail());
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Transactional
    public String resendOtp(EmailRequestDto request) {
        User user = getUser(request.getEmail());
        sendOtp(user);
        return ResponseMessages.OTP_SENT_MESSAGE;
    }

    @Transactional
    public AuthResponseDto loginOrRegisterOAuth2User(User user) {
        User entity;
        try {
             entity = getUser(user.getEmail());
        } catch (RuntimeException e) {
            entity = userRepository.save(user);
        }
        return generateNewToken(entity);
    }

    @Transactional
    public String changePassword(ChangePasswordRequestDto request) {
        User entity = getEntity();
        if(!passwordEncoder.matches(request.getOldPassword(), entity.getPassword())) {
            throw new InvalidInputException("Old password is incorrect");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("New password and confirm password are not equal");
        }
        entity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(entity);
        refreshTokenService.deleteRefreshTokenByUserId(entity.getId());
        return ResponseMessages.PASSWORD_CHANGED;
    }

    AuthResponseDto generateNewToken(User user) {

        refreshTokenService.deleteRefreshTokenByUserId(user.getId());

        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
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

    private User getEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
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