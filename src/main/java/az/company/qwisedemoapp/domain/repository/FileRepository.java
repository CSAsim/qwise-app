package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, JpaSpecificationExecutor<File>{

    @Query(value = "SELECT p FROM Packet p WHERE p.status != 'DELETED'")
    Optional<File> findByIdWithStatus(Long id);
    boolean existsById(Long id);
}
