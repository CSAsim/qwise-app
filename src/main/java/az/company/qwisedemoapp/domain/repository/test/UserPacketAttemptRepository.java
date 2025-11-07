package az.company.qwisedemoapp.domain.repository.test;

import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPacketAttemptRepository extends JpaRepository<UserPacketAttempt, Long> {

    Page<UserPacketAttempt> findAllByUserId(Long userId, Pageable pageable);
    Optional<UserPacketAttempt> findByIdAndUserPacketIdAndStatus(Long id, Long userPacketId, AttemptStatus status);
}
