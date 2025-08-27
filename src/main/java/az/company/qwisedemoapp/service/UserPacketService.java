package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.domain.repository.UserPacketRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.AlreadyExistsException;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.mapper.UserPacketMapper;
import az.company.qwisedemoapp.model.constants.ExceptionMessages;
import az.company.qwisedemoapp.model.dto.UserPacketDto;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import az.company.qwisedemoapp.model.request.AssignPacketRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserPacketService {

    private final UserPacketRepository userPacketRepository;
    private final UserRepository userRepository;
    private final PacketRepository packetRepository;
    private final UserPacketMapper userPacketMapper;

    public Page<UserPacketDto> findAllUserPackets(PacketUsageStatus status, Pageable pageable) {
        Page<UserPacket> entities = userPacketRepository.findAllByUsageStatus(status, pageable);
        return userPacketMapper.toDtoPage(entities);
    }

    @Transactional
    public UserPacketDto assignPacketToUser(AssignPacketRequest request) {
        Long currentUserId = AuthService.getCurrentUserId();
        User student = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("Student" + ExceptionMessages.NOT_FOUND));
        Packet packet = packetRepository.findPublishedPacketsById(request.getPacketId())
                .orElseThrow(() -> new NotFoundException("Packet" + ExceptionMessages.NOT_FOUND));
        boolean alreadyAssigned = student.getEnrolledPackets()
                .stream()
                .anyMatch(up -> up.getPacket().getId().equals(request.getPacketId()));
        if (alreadyAssigned) {
            throw new AlreadyExistsException("User packet already assigned");
        }
        UserPacket userPacket = UserPacket.builder()
                .usageStatus(PacketUsageStatus.STORE)
                .progress(0.0f)
                .build();
        packet.addEnrolledStudent(userPacket);
        student.addEnrolledPacket(userPacket);
        UserPacket savedUserPacket = userPacketRepository.save(userPacket);
        return userPacketMapper.toDto(savedUserPacket);
    }

    @Transactional
    public void deleteUserPacket(Long packetId) {
        log.info("Deleting user packet with id {}", packetId);
        Long userId = AuthService.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        UserPacket userPacket = user.getEnrolledPackets().stream()
                .filter(up -> up.getPacket().getId().equals(packetId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User packet" + ExceptionMessages.NOT_FOUND));
        user.removeEnrolledPacket(userPacket);
        userRepository.save(user);
        log.info("User packet deleted");
    }
}
