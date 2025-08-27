package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.Packet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacketRepository extends JpaRepository<Packet, Long>, JpaSpecificationExecutor<Packet> {

    @Query(value = "SELECT p FROM Packet p WHERE p.status != 'DELETED'")
    Optional<Packet> findByIdWithStatus(Long id);

    @Query(value = "SELECT p FROM Packet p WHERE p.id = :id AND p.status = 'PUBLISHED'")
    Optional<Packet> findPublishedPacketsById(Long id);
}
