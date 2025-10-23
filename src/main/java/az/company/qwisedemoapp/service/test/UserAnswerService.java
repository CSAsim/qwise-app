package az.company.qwisedemoapp.service.test;

import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.entity.test.answer.UserAnswer;
import az.company.qwisedemoapp.domain.repository.UserPacketRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.mapper.UserAnswerMapper;
import az.company.qwisedemoapp.model.dto.request.test.UserPacketFinishAttemptRequestDto;
import az.company.qwisedemoapp.model.dto.request.test.answer.UserAnswerRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import az.company.qwisedemoapp.model.dto.response.test.answer.UserAnswerResponseDto;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAnswerService {

    private final UserPacketRepository userPacketRepository;
    private final UserAnswerMapper userAnswerMapper;

    @Transactional
    public UserAnswerResponseDto saveUserAnswer(UserPacketAttempt attempt, List<UserAnswerRequestDto> answers) {

        UserPacket userPacket = attempt.getUserPacket();


        List<UserAnswer> userAnswers = userAnswerMapper.toEntityList(attempt, answers);
        attempt.getAnswers().clear();
        for (UserAnswer a : userAnswers) {
            attempt.addAnswer(a);
        }
        userPacket.setProgress(0.0f);
        userPacket.setUsageStatus(PacketUsageStatus.COMPLETED);

        UserPacket savedPacket = userPacketRepository.save(userPacket);

        return UserAnswerResponseDto.builder()
                .userPacketId(savedPacket.getId())
                .answers(null)
                .build();
    }

    public static Float calculateProgress(List<AnswerResponse> answers) {
        Float progress = 0.0f;
        for(AnswerResponse answer : answers) {
            progress += answer.getScore();
        }
        return progress;
    }
}
