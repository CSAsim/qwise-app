package az.company.qwisedemoapp.service.test;

import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.entity.test.answer.*;
import az.company.qwisedemoapp.mapper.UserAnswerMapper;
import az.company.qwisedemoapp.model.dto.request.test.answer.UserAnswerRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAnswerService {

    private final UserAnswerMapper userAnswerMapper;

    @Transactional
    public void saveUserAnswer(UserPacketAttempt attempt, List<UserAnswerRequestDto> answers) {
        log.info("Saving user answers for attempt {}", attempt.getId());

        if (answers == null || answers.isEmpty()) {
            return;
        }
        // Mövcud cavabları xəritəyə salırıq (questionId -> UserAnswer)
        Map<Long, UserAnswer> existingAnswers = attempt.getAnswers().stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a));

        for (UserAnswerRequestDto dto : answers) {
            Long questionId = dto.getQuestionId();

            // Əgər bu suala artıq cavab varsa — update et
            if (existingAnswers.containsKey(questionId)) {
                UserAnswer existing = existingAnswers.get(questionId);

                // Yeni cavabı (status, seçilmiş variant və s.) mapper-dən götür
                UserAnswer updated = userAnswerMapper.toEntity(attempt, dto);

                // Lazımi sahələri yenilə
                existing.setStatus(updated.getStatus());

                switch (existing) {
                    case ClosedAnswer closedAnswer when updated instanceof ClosedAnswer ->
                            closedAnswer.setOption(((ClosedAnswer) updated).getOption());
                    case OpenAnswer openAnswer when updated instanceof OpenAnswer ->
                            openAnswer.setAnswer(((OpenAnswer) updated).getAnswer());
                    case MatchingAnswer matchingAnswer when updated instanceof MatchingAnswer ->
                            matchingAnswer.setSelections(((MatchingAnswer) updated).getSelections());
                    case InterviewAnswer interviewAnswer when updated instanceof InterviewAnswer ->
                            interviewAnswer.setAnswer(((InterviewAnswer) updated).getAnswer());
                    default -> {
                    }
                }

            } else {
                UserAnswer newAnswer = userAnswerMapper.toEntity(attempt, dto);
                newAnswer.setAttempt(attempt);
                attempt.getAnswers().add(newAnswer);
            }
        }
    }


    public static Integer calculateScore(List<AnswerResponse> answers) {
        Integer score = 0;
        for (AnswerResponse answer : answers) {
            score += answer.getScore();
        }
        return score;
    }
}
