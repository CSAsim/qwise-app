package az.company.qwisedemoapp.domain.repository.packet;

import az.company.qwisedemoapp.domain.entity.packet.Packet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacketRepository extends JpaRepository<Packet, Long>, JpaSpecificationExecutor<Packet> {

    @Query(value = "SELECT p FROM Packet p WHERE p.status != 'DELETED' AND p.id = :id")
    Optional<Packet> findByIdWithStatus(Long id);

    @Query("""
        SELECT p FROM Packet p
        WHERE LOWER(p.name) LIKE %:q%
           OR LOWER(p.description) LIKE %:q%
           OR LOWER(p.category.name) LIKE %:q%
           OR LOWER(p.subcategory.name) LIKE %:q%
           OR LOWER(p.authorName) LIKE %:q%
        """)
    Page<Packet> search(@Param("q") String q, Pageable pageable);

    @Query(value = "SELECT p FROM Packet p WHERE p.id = :id AND p.status = 'PUBLISHED'")
    Optional<Packet> findPublishedPacketsById(Long id);
}
