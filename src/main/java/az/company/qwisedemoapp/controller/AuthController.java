package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.RegisterUserRequestDto;
import az.company.qwisedemoapp.model.dto.response.AuthResponseDto;
import az.company.qwisedemoapp.model.dto.request.ChangePasswordRequestDto;
import az.company.qwisedemoapp.model.dto.request.EmailRequestDto;
import az.company.qwisedemoapp.model.dto.request.LoginUserRequestDto;
import az.company.qwisedemoapp.model.dto.request.RefreshTokenRequestDto;
import az.company.qwisedemoapp.model.dto.request.ResetPasswordRequestDto;
import az.company.qwisedemoapp.model.dto.request.VerifyOtpRequestDto;
import az.company.qwisedemoapp.service.auth.AuthService;
import az.company.qwisedemoapp.service.auth.PasswordResetTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginUserRequestDto request, HttpServletRequest httpServletRequest) {
        log.info(httpServletRequest.getRemoteAddr());
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody RefreshTokenRequestDto request) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto request) {
        return ResponseEntity.ok(authService.verifyOtpCode(request));
    }

    @PostMapping("/verify-reset-token")
    public ResponseEntity<Void> verifyPasswordResetToken(@Valid @RequestBody VerifyOtpRequestDto request) {
        authService.verifyPasswordResetToken(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody EmailRequestDto request) {
        passwordResetTokenService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        passwordResetTokenService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        return ResponseEntity.ok(authService.changePassword(request));
    }

    @PostMapping("/resend-otp")
    public  ResponseEntity<String> resendOtp(@Valid @RequestBody EmailRequestDto request) {
        return ResponseEntity.ok(authService.resendOtp(request));
    }
}
