package az.company.qwisedemoapp.service.test;

import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.repository.UserPacketRepository;
import az.company.qwisedemoapp.domain.repository.test.UserPacketAttemptRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.mapper.QuestionMapper;
import az.company.qwisedemoapp.mapper.UserAnswerMapper;
import az.company.qwisedemoapp.mapper.UserPacketAttemptMapper;
import az.company.qwisedemoapp.model.dto.request.test.StartAttemptRequestDto;
import az.company.qwisedemoapp.model.dto.response.attempt.ResultAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.request.test.AttemptRequestDto;
import az.company.qwisedemoapp.model.dto.response.attempt.ResumeAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.response.attempt.StartAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import az.company.qwisedemoapp.util.MathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttemptService {

    private final UserPacketAttemptRepository userPacketAttemptRepository;
    private final UserPacketAttemptMapper userPacketAttemptMapper;
    private final QuestionMapper questionMapper;
    private final UserPacketRepository userPacketRepository;
    private final UserAnswerService userAnswerService;
    private final UserAnswerMapper userAnswerMapper;

    @Transactional
    public StartAttemptResponseDto startAttempt(StartAttemptRequestDto request) {
        log.info("Starting attempt for user packet {}", request.getUserPacketId());
        UserPacket userPacket = userPacketRepository.findById(request.getUserPacketId())
                .orElseThrow(() -> new NotFoundException("User packet not found"));
        userPacket.setStatus(PacketUsageStatus.ONGOING);

        UserPacketAttempt attempt = UserPacketAttempt.builder()
                .user(userPacket.getUser())
                .userPacket(userPacket)
                .attemptNumber(userPacket.getAttempts() != null ? userPacket.getAttempts().size() + 1 : 1)
                .status(AttemptStatus.IN_PROGRESS)
                .startedAt(LocalDateTime.now())
                .lastResumedAt(LocalDateTime.now())
                .build();

        if (userPacket.getAttempts() == null) {
            userPacket.setAttempts(new ArrayList<>());
        }
        userPacket.getAttempts().add(attempt);

        userPacketAttemptRepository.save(attempt);
        List<QuestionResponseDto> questionResponses = questionMapper
                .toResponseList(userPacket.getResource().getQuestions());
        log.info("Attempt started at {}", attempt.getStartedAt());

        return StartAttemptResponseDto.builder()
                .attemptId(attempt.getId())
                .userPacketId(attempt.getUserPacket().getId())
                .packetName(userPacket.getResource().getName())
                .questionResponse(questionResponses)
                .build();
    }


    @Transactional
    public ResultAttemptResponseDto pauseAttempt(AttemptRequestDto requestDto) {
        log.info("Pausing attempt {}", requestDto.getAttemptId());

        UserPacketAttempt attempt = userPacketAttemptRepository.findById(requestDto.getAttemptId())
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
        attempt.getUserPacket().setStatus(PacketUsageStatus.ONGOING);
        long diff = Duration.between(attempt.getLastResumedAt(), LocalDateTime.now()).toMinutes();
        attempt.setDuration(attempt.getDuration() + diff);
        attempt.setFinishedAt(LocalDateTime.now());
        attempt.setStatus(AttemptStatus.PAUSED);
        userAnswerService.saveUserAnswer(attempt, requestDto.getAnswers());
        attempt = userPacketAttemptRepository.save(attempt);
        List<AnswerResponse> answerResponseDto = userAnswerMapper.toDtoList(attempt.getAnswers());
        float progress = MathUtil.calculateProgress(answerResponseDto);
        attempt.getUserPacket().setProgress(progress);
        userPacketRepository.save(attempt.getUserPacket());

        log.info("Attempt paused at {}", attempt.getFinishedAt());
        return userPacketAttemptMapper.toDto(attempt, answerResponseDto);
    }

    @Transactional
    public ResumeAttemptResponseDto resumeAttempt(Long attemptId) {
        UserPacketAttempt attempt = userPacketAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        attempt.setLastResumedAt(LocalDateTime.now());
        attempt = userPacketAttemptRepository.save(attempt);
        List<AnswerResponse> answerResponseDto = userAnswerMapper.toDtoList(attempt.getAnswers());
        log.info("Attempt resumed at {}", attempt.getStartedAt());
        return ResumeAttemptResponseDto.builder()
                .attemptId(attempt.getId())
                .packetName(attempt.getUserPacket().getResource().getName())
                .lastModifiedAnswers(answerResponseDto)
                .build();
    }

    @Transactional
    public ResultAttemptResponseDto finishAttempt(AttemptRequestDto request) {
        log.info("Finishing attempt {}", request.getAttemptId());
        UserPacketAttempt attempt = userPacketAttemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
        attempt.getUserPacket().setStatus(PacketUsageStatus.COMPLETED);
        userAnswerService.saveUserAnswer(attempt, request.getAnswers());
        List<AnswerResponse> answerResponses = userAnswerMapper.toDtoList(attempt.getAnswers());
        updateAttempt(attempt, answerResponses);
        attempt.setStatus(AttemptStatus.COMPLETED);
        attempt = userPacketAttemptRepository.save(attempt);
        answerResponses = userAnswerMapper.toDtoList(attempt.getAnswers());
        ResultAttemptResponseDto responseDto = userPacketAttemptMapper.toDto(attempt, answerResponses);
        log.info("Attempt finished at {}", attempt.getFinishedAt());
        return responseDto;
    }

    private void updateAttempt(UserPacketAttempt attempt, List<AnswerResponse> answerResponses) {
        Integer totalScore = UserAnswerService.calculateScore(answerResponses);
        Integer correctAnswers = MathUtil.calculateTotalCorrectQuestions(answerResponses);
        Integer skippedAnswers = MathUtil.calculateTotalSkippedQuestions(answerResponses);
        Integer incorrectAnswers = MathUtil.calculateTotalIncorrectQuestions(answerResponses);
        attempt.setTotalCorrectAnswerCount(correctAnswers);
        attempt.setTotalSkippedAnswerCount(skippedAnswers);
        attempt.setTotalWrongAnswerCount(incorrectAnswers);
        attempt.setTotalScore(totalScore);
        attempt.setFinishedAt(LocalDateTime.now());

        long diff;
        if (attempt.getLastResumedAt() != null) {
            diff = Duration.between(attempt.getLastResumedAt(), LocalDateTime.now()).toMinutes();
        } else {
            diff = Duration.between(attempt.getStartedAt(), LocalDateTime.now()).toMinutes();
        }
        attempt.setDuration(attempt.getDuration() + diff);
    }
}
