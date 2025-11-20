package az.company.qwisedemoapp.domain.repository.packet;

import az.company.qwisedemoapp.domain.entity.packet.PacketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacketCategoryRepository extends JpaRepository<PacketCategory, Long> {

    List<PacketCategory> findAllByOrderByNameAsc();

    boolean existsByNameIgnoreCase(String name);
}
