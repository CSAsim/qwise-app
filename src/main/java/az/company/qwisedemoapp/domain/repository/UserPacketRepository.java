package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPacketRepository extends JpaRepository<UserPacket, Long> {

    Page<UserPacket> findAll(Specification<UserPacket> specification, Pageable sortedPageable);
}
