package az.company.qwisedemoapp.domain.repository.test;

import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPacketAttemptRepository extends JpaRepository<UserPacketAttempt, Long> {

    Optional<UserPacketAttempt> findByUserIdAndUserPacketPacketId(Long userId, Long packetId);
}
