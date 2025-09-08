package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.filter.PacketSpecificationFilter;
import az.company.qwisedemoapp.mapper.PacketMapper;
import az.company.qwisedemoapp.model.constants.ExceptionMessages;
import az.company.qwisedemoapp.model.dto.PacketDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import az.company.qwisedemoapp.model.request.CreatePacketRequest;
import az.company.qwisedemoapp.model.request.FilteredPacketRequest;
import az.company.qwisedemoapp.model.request.UpdatePacketRequest;
import az.company.qwisedemoapp.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PacketService {

    private final PacketRepository packetRepository;
    private final UserRepository userRepository;
    private final PacketMapper packetMapper;

    public Page<PacketDto> findAllPackets(FilteredPacketRequest request, Pageable pageable) {
        Specification<Packet> specification = PacketSpecificationFilter.byFilters(request);
        Page<Packet> page = packetRepository.findAll(specification, pageable);
        return packetMapper.toDtoPage(page);
    }

    public PacketDto findById(Long id) {
        return packetMapper.toDto(packetRepository.findByIdWithStatus(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND)));
    }

    @Transactional
    public PacketDto createPacket(CreatePacketRequest request) {
        log.info("Creating packet: {}", request);
        Long authorId = AuthService.getCurrentUserId();
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        Packet packet = packetMapper.toEntity(request);
        author.addPacket(packet);
        packet.setRating(0.0f);
        Packet savedPacket = packetRepository.save(packet);
        log.info("Packet created: {}", savedPacket);
        return packetMapper.toDto(savedPacket);
    }

    @Transactional
    public PacketDto publishPacket(Long id) {
        log.info("Publishing packet with id {}", id);
        Packet packet = packetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        packet.setStatus(PacketStatus.PUBLISHED);
        Packet savedPacket = packetRepository.save(packet);
        return packetMapper.toDto(savedPacket);
    }

    @Transactional
    public PacketDto updatePacket(Long id, UpdatePacketRequest request) {
        log.info("Updating packet with id {}: {}", id, request);
        Packet oldPacket = packetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        Packet newPacket = packetMapper.toEntity(request, oldPacket);
        Packet savedPacket = packetRepository.save(newPacket);
        log.info("Packet updated: {}", savedPacket);
        return packetMapper.toDto(savedPacket);
    }

    @Transactional
    public void deletePacket(Long id) {
        log.info("Deleting packet with id {}", id);
        Packet packet = packetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        packet.setStatus(PacketStatus.DELETED);
        packetRepository.save(packet);
        log.info("Packet deleted");
    }

}
