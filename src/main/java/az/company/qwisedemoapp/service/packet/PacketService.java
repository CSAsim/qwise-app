package az.company.qwisedemoapp.service.packet;

import az.company.qwisedemoapp.domain.entity.packet.Packet;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.filter.PacketSpecificationFilter;
import az.company.qwisedemoapp.mapper.PacketMapper;
import az.company.qwisedemoapp.model.constants.ExceptionMessages;
import az.company.qwisedemoapp.model.dto.request.packet.UpdatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.packet.PacketDetailResponseDto;
import az.company.qwisedemoapp.model.dto.response.packet.PacketListResponseDto;
import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import az.company.qwisedemoapp.model.dto.request.packet.CreatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.service.auth.AuthService;
import az.company.qwisedemoapp.service.test.QuestionService;
import az.company.qwisedemoapp.util.SortUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<PacketListResponseDto> findAllPacketsByFilterRequest(FilteredRequestDto request, Pageable pageable) {
        Specification<Packet> specification = PacketSpecificationFilter.byFilters(request);
        Page<Packet> page = packetRepository.findAll(specification, pageable);

        return page.map(packet -> {
            PacketListResponseDto dto = packetMapper.toDto(packet);
            dto.setTotalQuestionCount(packet.getQuestions() != null ? packet.getQuestions().size() : 0);
            return dto;
        });
    }

    public Page<PacketListResponseDto> findAllPacketsByFilter(String sort, Pageable pageable) {
        Sort sorting = SortUtil.resolveSort(sort);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
        Page<Packet> pages = packetRepository.findAll(sortedPageable);
        return pages.map(packet -> {
            PacketListResponseDto dto = packetMapper.toDto(packet);
            dto.setTotalQuestionCount(packet.getQuestions() != null ? packet.getQuestions().size() : 0);
            return dto;
        });
    }

    public Page<PacketListResponseDto> findAllPacketsBySearch(String query, Pageable pageable) {
        Page<Packet> pages = packetRepository.search(query, pageable);
        return pages.map(packet -> {
            PacketListResponseDto dto = packetMapper.toDto(packet);
            dto.setTotalQuestionCount(packet.getQuestions() != null ? packet.getQuestions().size() : 0);
            return dto;
        });
    }

    public PacketDetailResponseDto findById(Long id) {
        Packet packet = packetRepository.findByIdWithStatus(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        PacketDetailResponseDto responseDto = packetMapper.toDtoDetail(packet);
        responseDto.setTotalQuestionCount(packet.getQuestions().size());
        return responseDto;
    }

    @Transactional
    public PacketDetailResponseDto createPacket(CreatePacketRequestDto request) {
        log.info("Creating packet: {}", request);

        User author = userRepository.findById(AuthService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Packet packet = packetMapper.toEntity(request);
        packet.setAuthor(author);
        packet.setStatus(PacketStatus.ACTIVE);

        Packet saved = packetRepository.save(packet);
        questionService.createQuestions(saved, request.getQuestions());
        PacketDetailResponseDto response = packetMapper.toDtoDetail(saved);
        response.setTotalQuestionCount(request.getQuestions().size());
        return response;
    }

    @Transactional
    public PacketDetailResponseDto publishPacket(Long id) {
        log.info("Publishing packet with id {}", id);
        Packet packet = packetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        packet.setStatus(PacketStatus.PUBLISHED);
        Packet savedPacket = packetRepository.save(packet);
        PacketDetailResponseDto response = packetMapper.toDtoDetail(savedPacket);
        response.setTotalQuestionCount(packet.getQuestions().size());
        log.info("Packet published: {}", savedPacket);
        return response;
    }

    @Transactional
    public PacketDetailResponseDto updatePacket(Long id, UpdatePacketRequestDto request) {
        log.info("Updating packet with id {}: {}", id, request);
        Packet oldPacket = packetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Packet.class.getSimpleName() + ExceptionMessages.NOT_FOUND));
        Packet newPacket = packetMapper.toEntity(request, oldPacket);
        Packet savedPacket = packetRepository.save(newPacket);
        List<QuestionResponseDto> questions = questionService.updateQuestions(savedPacket, request.getQuestions());
        PacketDetailResponseDto response = packetMapper.toDtoDetail(savedPacket);
        response.setTotalQuestionCount(request.getQuestions().size());
        log.info("Packet updated: {}", savedPacket);
        return response;
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
