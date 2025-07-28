package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    List<OtpCode> findByExpirationDateTimeBefore(LocalDateTime expirationDateTimeBefore);
    Optional<OtpCode> findByCode(String code);
    Optional<OtpCode> findByUserId(Long userId);
}
