package az.company.qwisedemoapp.service;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.test.question.Question;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.domain.repository.test.MatchingPairRepository;
import az.company.qwisedemoapp.domain.repository.test.MatchingSelectionRepository;
import az.company.qwisedemoapp.domain.repository.test.OptionRepository;
import az.company.qwisedemoapp.domain.repository.test.QuestionRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.filter.PacketSpecificationFilter;
import az.company.qwisedemoapp.mapper.PacketMapper;
import az.company.qwisedemoapp.mapper.QuestionMapper;
import az.company.qwisedemoapp.model.constants.ExceptionMessages;
import az.company.qwisedemoapp.model.dto.request.UpdatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.PacketResponseDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import az.company.qwisedemoapp.model.dto.request.CreatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.service.auth.AuthService;
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
    private final QuestionMapper questionMapper;
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
        Long authorId = AuthService.getCurrentUserId();
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        Packet packet = packetMapper.toEntity(request);
        packet.setStatus(PacketStatus.ACTIVE);
        author.addPacket(packet);
        packet.setRating(0.0f);

        List<Question> questions = questionMapper.toEntityList(request.getQuestions());
        questions.forEach(q -> q.setPacket(packet));
        packet.setQuestions(questions);

        Packet savedPacket = packetRepository.save(packet);
        log.info("Packet created: {}", savedPacket);
        return packetMapper.toDto(savedPacket);
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
        if(request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            List<Question> questions = questionMapper.toEntityList(request.getQuestions());
            questions.forEach(q -> q.setPacket(newPacket));
            newPacket.setQuestions(questions);
        }
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
