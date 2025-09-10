package az.company.qwisedemoapp.service.auth;

import az.company.qwisedemoapp.domain.entity.OtpCode;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.OtpCodeRepository;
import az.company.qwisedemoapp.exception.InvalidInputException;
import az.company.qwisedemoapp.model.enums.OtpCodeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OtpCodeService {

    private final OtpCodeRepository otpCodeRepository;

    private static final int OTP_LENGTH = 6;
    private static final int EXPIRATION_MINUTES = 5;

    @Transactional
    public OtpCode createOtpCode(User user) {
        String code = generateOtpCode();

        OtpCode token = OtpCode.builder()
                .code(code)
                .user(user)
                .expirationDateTime(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))
                .status(OtpCodeStatus.ACTIVE)
                .build();

        return otpCodeRepository.save(token);
    }

    @Transactional
    public void validateOtp(String code) {
        OtpCode token = otpCodeRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInputException("OTP not found"));

        if (token.getStatus().equals(OtpCodeStatus.DEACTIVATED)) {
            throw new InvalidInputException("OTP code has expired");
        }

        token.setStatus(OtpCodeStatus.DEACTIVATED);
        log.info("Before save - OTP status: {}", token.getStatus());
        otpCodeRepository.save(token);
        log.info("After save - OTP status: {}", token.getStatus());
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanUpExpiredOtpCodes() {
        List<OtpCode> otpCodes = otpCodeRepository.findByExpirationDateTimeBefore(LocalDateTime.now());
        otpCodes.forEach(obj -> obj.setStatus(OtpCodeStatus.DEACTIVATED));
        otpCodeRepository.saveAll(otpCodes);
    }

    @Transactional
    public void changeOtpStatus(Long userId) {
        otpCodeRepository.updateOtpCodeStatusByUserId(userId, OtpCodeStatus.DEACTIVATED);
    }

    private String generateOtpCode() {
        Random random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

}
