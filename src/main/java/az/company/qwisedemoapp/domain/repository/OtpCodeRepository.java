package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.OtpCode;
import az.company.qwisedemoapp.model.enums.OtpCodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    List<OtpCode> findByExpirationDateTimeBefore(LocalDateTime expirationDateTimeBefore);
    Optional<OtpCode> findByCode(String code);

    @Modifying
    @Query("UPDATE OtpCode o SET o.status = :status WHERE o.user.id = :userId")
    void updateOtpCodeStatusByUserId(@Param("userId") Long userId, @Param("status") OtpCodeStatus status);
}
