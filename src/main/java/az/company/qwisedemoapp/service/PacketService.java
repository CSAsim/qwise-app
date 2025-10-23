package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.filter.PacketSpecificationFilter;
import az.company.qwisedemoapp.mapper.PacketMapper;
import az.company.qwisedemoapp.model.constants.ExceptionMessages;
import az.company.qwisedemoapp.model.dto.request.UpdatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.PacketResponseDto;
import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import az.company.qwisedemoapp.model.dto.request.CreatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.service.auth.AuthService;
import az.company.qwisedemoapp.service.test.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PacketService {

    private final PacketRepository packetRepository;
    private final UserRepository userRepository;
    private final QuestionService questionService;
    private final PacketMapper packetMapper;

    public Page<PacketResponseDto> findAllPackets(FilteredRequestDto request, Pageable pageable) {
        Specification<Packet> specification = PacketSpecificationFilter.byFilters(request);
        Page<Packet> page = packetRepository.findAll(specification, pageable);
        return packetMapper.toDtoPage(page);
    }

    public PacketResponseDto findById(Long id) {
        return packetMapper.toDto(packetRepository.findByIdWithStatus(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND)));
    }

    @Transactional
    public PacketResponseDto createPacket(CreatePacketRequestDto request) {
        log.info("Creating packet: {}", request);

        User author = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Packet packet = packetMapper.toEntity(request);
        packet.setAuthor(author);
        packet.setStatus(PacketStatus.ACTIVE);
        packet.setRating(0.0f);

        Packet saved = packetRepository.save(packet);
        List<QuestionResponseDto> questionResponseDto = questionService.createQuestions(saved, request.getQuestions());
        PacketResponseDto response = packetMapper.toDto(saved);
        response.setQuestions(questionResponseDto);
        return response;
    }

    @Transactional
    public PacketResponseDto publishPacket(Long id) {
        log.info("Publishing packet with id {}", id);
        Packet packet = packetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        packet.setStatus(PacketStatus.PUBLISHED);
        Packet savedPacket = packetRepository.save(packet);
        return packetMapper.toDto(savedPacket);
    }

    @Transactional
    public PacketResponseDto updatePacket(Long id, UpdatePacketRequestDto request) {
        log.info("Updating packet with id {}: {}", id, request);
        Packet oldPacket = packetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        Packet newPacket = packetMapper.toEntity(request, oldPacket);
        Packet savedPacket = packetRepository.save(newPacket);
        List<QuestionResponseDto> questions = questionService.updateQuestions(savedPacket, request.getQuestions());
        PacketResponseDto response = packetMapper.toDto(savedPacket);
        response.setQuestions(questions);
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
