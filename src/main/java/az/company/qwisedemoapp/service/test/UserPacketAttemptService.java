package az.company.qwisedemoapp.service.test;

import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.repository.UserRepository;
import az.company.qwisedemoapp.domain.repository.test.UserPacketAttemptRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.mapper.UserAnswerMapper;
import az.company.qwisedemoapp.mapper.UserPacketAttemptMapper;
import az.company.qwisedemoapp.model.dto.request.test.UserPacketAttemptRequestDto;
import az.company.qwisedemoapp.model.dto.request.test.UserPacketAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.request.test.UserPacketFinishAttemptRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import az.company.qwisedemoapp.model.dto.response.test.answer.UserAnswerResponseDto;
import az.company.qwisedemoapp.model.enums.AnswerStatus;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
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
public class UserPacketAttemptService {

    private final UserRepository userRepository;
    private final UserPacketAttemptRepository userPacketAttemptRepository;
    private final UserAnswerService userAnswerService;
    private final UserPacketAttemptMapper userPacketAttemptMapper;
    private final UserAnswerMapper userAnswerMapper;

    @Transactional
    public UserPacketAttemptResponseDto startAttempt(UserPacketAttemptRequestDto request) {
        log.info("Starting attempt for user {} and packet {}", request.getUserId(), request.getUserPacketId());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        UserPacket userPacket = user.getEnrolledPackets()
                .stream()
                .filter(u -> u.getId().equals(request.getUserPacketId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User packet not found"));

        UserPacketAttempt attempt = UserPacketAttempt.builder()
                .user(user)
                .userPacket(userPacket)
                .attemptNumber(userPacket.getAttempts() != null ? userPacket.getAttempts().size() + 1 : 1)
                .status(AttemptStatus.IN_PROGRESS)
                .startedAt(LocalDateTime.now())
                .build();

        if (userPacket.getAttempts() == null) {
            userPacket.setAttempts(new ArrayList<>());
        }
        userPacket.getAttempts().add(attempt);

        userPacketAttemptRepository.save(attempt);
        log.info("Attempt started at {}", attempt.getStartedAt());

        return userPacketAttemptMapper.toResponseDto(attempt);
    }

    @Transactional
    public UserPacketAttemptResponseDto finishAttempt(UserPacketFinishAttemptRequestDto request) {
        log.info("Finishing attempt {}", request.getAttemptId());
        UserPacketAttempt attempt = userPacketAttemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
        UserAnswerResponseDto response = userAnswerService
                .saveUserAnswer(attempt, request.getAnswers());
        List<AnswerResponse> answerResponses =  userAnswerMapper.toDtoList(attempt.getAnswers());

        updateAttempt(attempt, answerResponses);

        attempt = userPacketAttemptRepository.save(attempt);

        answerResponses = userAnswerMapper.toDtoList(attempt.getAnswers());
        response.setAnswers(answerResponses);
        UserPacketAttemptResponseDto responseDto = userPacketAttemptMapper.toResponseDto(attempt);
        responseDto.setDuration(attempt.getDuration());
        responseDto.setResult(response);
        log.info("Attempt finished at {}", attempt.getFinishedAt());
        return responseDto;
    }

    private void updateAttempt(UserPacketAttempt attempt, List<AnswerResponse> answerResponses) {
        Float totalScore = UserAnswerService.calculateProgress(answerResponses);
        Integer correctAnswers = calculateTotalCorrectQuestions(answerResponses);
        Integer skippedAnswers = calculateTotalSkippedQuestions(answerResponses);
        Integer incorrectAnswers = calculateTotalIncorrectQuestions(answerResponses);
        attempt.setTotalCorrectAnswerCount(correctAnswers);
        attempt.setTotalSkippedAnswerCount(skippedAnswers);
        attempt.setTotalWrongAnswerCount(incorrectAnswers);
        attempt.setTotalScore(totalScore);
        attempt.setStatus(AttemptStatus.COMPLETED);
        attempt.setFinishedAt(LocalDateTime.now());

        Duration duration = Duration.between(attempt.getStartedAt(), attempt.getFinishedAt());
        String durationString = formatDuration(duration);
        attempt.setDuration(durationString);
    }

    private String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        if (hours > 0) {
            return String.format("%d saat %d dəqiqə", hours, remainingMinutes);
        } else {
            return String.format("%d dəqiqə", remainingMinutes);
        }
    }

    private Integer calculateTotalCorrectQuestions(List<AnswerResponse> responses) {
        return (int) responses.stream().filter(a -> a.getStatus() == AnswerStatus.CORRECT).count();
    }

    private Integer calculateTotalSkippedQuestions(List<AnswerResponse> responses) {
        return (int) responses.stream().filter(a -> a.getStatus() == AnswerStatus.SKIPPED).count();
    }

    private Integer calculateTotalIncorrectQuestions(List<AnswerResponse> responses) {
        return (int) responses.stream().filter(a -> a.getStatus() == AnswerStatus.WRONG).count();
    }
}
