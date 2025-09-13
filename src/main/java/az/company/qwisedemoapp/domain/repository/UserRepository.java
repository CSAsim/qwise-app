package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByStatus(Pageable pageable, UserStatus status);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
