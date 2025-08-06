package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.AuthResponse;
import az.company.qwisedemoapp.model.request.EmailRequest;
import az.company.qwisedemoapp.model.request.LoginUserRequest;
import az.company.qwisedemoapp.model.request.RegisterUserRequest;
import az.company.qwisedemoapp.model.request.ResetPasswordRequest;
import az.company.qwisedemoapp.model.request.VerifyOtpRequest;
import az.company.qwisedemoapp.service.AuthService;
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
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginUserRequest request, HttpServletRequest httpServletRequest) {
        log.info(httpServletRequest.getRemoteAddr());
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtpCode(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody EmailRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PostMapping("/forgot-password/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    @PostMapping("/resend-otp")
    public  ResponseEntity<String> resendOtp(@Valid @RequestBody EmailRequest request) {
        return ResponseEntity.ok(authService.reSendOtpCode(request));
    }
}
