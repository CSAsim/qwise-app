package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.test.Option;
import az.company.qwisedemoapp.domain.entity.test.answer.*;
import az.company.qwisedemoapp.domain.entity.test.question.MatchingSelection;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.entity.test.question.*;
import az.company.qwisedemoapp.domain.repository.test.OptionRepository;
import az.company.qwisedemoapp.domain.repository.test.QuestionRepository;
import az.company.qwisedemoapp.domain.repository.test.UserAnswerRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import az.company.qwisedemoapp.model.dto.OptionDto;
import az.company.qwisedemoapp.model.dto.request.test.answer.*;
import az.company.qwisedemoapp.model.dto.response.test.answer.*;
import az.company.qwisedemoapp.model.enums.AnswerStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAnswerMapper {

    private final UserAnswerRepository userAnswerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final OptionRepository optionRepository;

    public UserAnswer toEntity(UserPacketAttempt attempt, UserAnswerRequestDto dto) {

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question not found"));
        return switch (dto.getQuestionType()) {
            case OPEN -> buildOpenAnswer(attempt, question, dto);
            case CLOSED -> buildClosedAnswer(attempt, question, dto);
            case MATCHING -> buildMatchingAnswer(attempt, question, dto);
            case INTERVIEW -> buildInterviewAnswer(attempt, question, dto);
        };
    }

    public List<UserAnswer> toEntityList(UserPacketAttempt attempt, List<UserAnswerRequestDto> dtoList) {
        List<UserAnswer> list = new ArrayList<>();
        for (UserAnswerRequestDto dto : dtoList) {
            list.add(toEntity(attempt, dto));
        }
        return list;
    }

    private AnswerResponse toDto(UserAnswer answer) {
        if (answer instanceof OpenAnswer open) {
            return getOpenAnswerResponseDto(answer, open);
        }
        if (answer instanceof ClosedAnswer closed) {
            ClosedAnswerResponseDto dto = new ClosedAnswerResponseDto();
            dto.setId(answer.getId());
            dto.setQuestion(questionMapper.toResponse(answer.getQuestion()));
            dto.setScore(closed.getStatus() == AnswerStatus.CORRECT ? 1 : 0);
            OptionDto optionDto = null;
            if (closed.getOption() != null) {
                optionDto = OptionDto.builder()
                        .id(closed.getOption().getId())
                        .text(closed.getOption().getText())
                        .correct(closed.getOption().isCorrect())
                        .build();
            }

            OptionDto correctOption = ((ClosedQuestion) answer.getQuestion()).getOptions()
                    .stream()
                    .filter(Option::isCorrect)
                    .findFirst()
                    .map(o -> new OptionDto(o.getId(), o.getText(), o.isCorrect()))
                    .orElse(null);

            dto.setStatus(closed.getStatus());
            dto.setYourOption(optionDto);
            dto.setCorrectOption(correctOption);
            return dto;
        }

        if (answer instanceof MatchingAnswer matching) {
            MatchingAnswerResponseDto dto = new MatchingAnswerResponseDto();
            dto.setId(answer.getId());
            dto.setQuestion(questionMapper.toResponse(answer.getQuestion()));
            dto.setScore(matching.getStatus() == AnswerStatus.CORRECT ? 1 : 0);

            List<MatchingSelectionDto> matchingSelections = new ArrayList<>();
            for (MatchingUserSelection s : matching.getSelections()) {
                matchingSelections.add(new MatchingSelectionDto(s.getLeftKey(), new ArrayList<>(s.getUserSelections())));
            }
            dto.setYourSelections(matchingSelections);

            List<MatchingSelectionDto> correctSelections = new ArrayList<>();
            for (MatchingSelection ms : ((MatchingQuestion) matching.getQuestion()).getCorrectSelections()) {
                correctSelections.add(new MatchingSelectionDto(ms.getLeftKey(), new ArrayList<>(ms.getChosenRightKeys())));
            }
            dto.setCorrectSelections(correctSelections);
            dto.setStatus(matching.getStatus());
            return dto;
        }

        if (answer instanceof InterviewAnswer interview) {
            InterviewAnswerResponseDto dto = new InterviewAnswerResponseDto();
            dto.setId(answer.getId());
            dto.setQuestion(questionMapper.toResponse(answer.getQuestion()));
            dto.setStatus(answer.getStatus());
            dto.setAnswer(interview.getAnswer());
            return dto;
        }
        throw new IllegalArgumentException("Unsupported answer type: " + answer.getClass());
    }

    @NotNull
    private OpenAnswerResponseDto getOpenAnswerResponseDto(UserAnswer answer, OpenAnswer open) {
        OpenAnswerResponseDto dto = new OpenAnswerResponseDto();
        dto.setId(answer.getId());
        dto.setQuestion(questionMapper.toResponse(answer.getQuestion()));
        dto.setScore(open.getStatus() == AnswerStatus.CORRECT ? 1 : 0);
        dto.setYourAnswer(open.getAnswer());
        dto.setCorrectAnswer(((OpenQuestion) answer.getQuestion()).getAnswer());
        dto.setStatus(open.getStatus());
        return dto;
    }

    public List<AnswerResponse> toDtoList(List<UserAnswer> entities) {
        List<AnswerResponse> list = new ArrayList<>();
        for (UserAnswer a : entities) {
            list.add(toDto(a));
        }
        return list;
    }

    private OpenAnswer buildOpenAnswer(UserPacketAttempt attempt, Question question, UserAnswerRequestDto dto) {
        OpenAnswerRequestDto openDto = (OpenAnswerRequestDto) dto;
        OpenAnswer answer = (OpenAnswer) userAnswerRepository.findByQuestionId(question.getId())
                .orElseGet(OpenAnswer::new);
        answer.setUserPacket(attempt.getUserPacket());
        answer.setQuestion(question);
        answer.setAttempt(attempt);
        answer.setAnswer(openDto.getYourAnswer());
        if (openDto.getYourAnswer() == null || openDto.getYourAnswer().isEmpty()) {
            answer.setStatus(AnswerStatus.SKIPPED);
            return answer;
        }
        answer.setStatus(openDto
                .getYourAnswer()
                .equalsIgnoreCase(((OpenQuestion) question)
                        .getAnswer()) ? AnswerStatus.CORRECT : AnswerStatus.WRONG);
        return answer;
    }

    private ClosedAnswer buildClosedAnswer(UserPacketAttempt attempt, Question question, UserAnswerRequestDto dto) {
        ClosedAnswerRequestDto closedDto = (ClosedAnswerRequestDto) dto;
        ClosedAnswer answer = (ClosedAnswer) userAnswerRepository.findByQuestionId(question.getId())
                .orElseGet(ClosedAnswer::new);
        answer.setUserPacket(attempt.getUserPacket());
        answer.setQuestion(question);
        answer.setAttempt(attempt);
        if (closedDto.getOptionId() == null) {
            answer.setStatus(AnswerStatus.SKIPPED);
            answer.setOption(null);
            return answer;
        }
        Option selectedOption = optionRepository.findById(closedDto.getOptionId())
                .orElseThrow(() -> new NotFoundException("Option not found"));
        answer.setOption(selectedOption);
        answer.setStatus(selectedOption.isCorrect() ? AnswerStatus.CORRECT : AnswerStatus.WRONG);

        return answer;
    }

    private MatchingAnswer buildMatchingAnswer(UserPacketAttempt attempt, Question question, UserAnswerRequestDto dto) {
        MatchingAnswerRequestDto matchingDto = (MatchingAnswerRequestDto) dto;
        MatchingAnswer answer = (MatchingAnswer) userAnswerRepository.findByQuestionId(question.getId())
                .orElseGet(MatchingAnswer::new);

        answer.setUserPacket(attempt.getUserPacket());
        answer.setQuestion(question);
        answer.setAttempt(attempt);

        // Əgər user heç nə göndərməyibsə — skipped
        if (matchingDto.getUserSelections() == null || matchingDto.getUserSelections().isEmpty()) {
            if (answer.getSelections() != null) {
                answer.getSelections().clear();
            }
            answer.setStatus(AnswerStatus.SKIPPED);
            return answer;
        }

        // Seçimləri yenidən qururuq
        List<MatchingUserSelection> newSelections = new ArrayList<>();
        for (MatchingSelectionDto s : matchingDto.getUserSelections()) {
            MatchingUserSelection userSelection = new MatchingUserSelection();
            userSelection.setQuestion((MatchingQuestion) question);
            userSelection.setAttempt(attempt);
            userSelection.setAnswer(answer);
            userSelection.setLeftKey(s.getLeftKey());
            userSelection.setUserSelections(new ArrayList<>(s.getChosenRightKeys()));
            newSelections.add(userSelection);
        }

        // Köhnə listi qoruyaraq yeniləri əlavə et
        if (answer.getSelections() == null) {
            answer.setSelections(new ArrayList<>());
        } else {
            answer.getSelections().clear();
        }
        answer.getSelections().addAll(newSelections);

        // Doğruluğu yoxla
        List<MatchingSelection> correctSelections = ((MatchingQuestion) question).getCorrectSelections();
        boolean isCorrect = newSelections.size() == correctSelections.size() &&
                newSelections.stream().allMatch(s -> correctSelections.stream()
                        .anyMatch(cs -> cs.getLeftKey().equals(s.getLeftKey())
                                && new HashSet<>(cs.getChosenRightKeys())
                                .equals(new HashSet<>(s.getUserSelections()))));

        answer.setStatus(isCorrect ? AnswerStatus.CORRECT : AnswerStatus.WRONG);
        return answer;
    }

    private InterviewAnswer buildInterviewAnswer(UserPacketAttempt attempt, Question question, UserAnswerRequestDto dto) {
        InterviewAnswerRequestDto requestDto = (InterviewAnswerRequestDto) dto;
        InterviewAnswer answer = new InterviewAnswer();
        answer.setUserPacket(attempt.getUserPacket());
        answer.setQuestion(question);
        answer.setAttempt(attempt);
        answer.setStatus(AnswerStatus.SKIPPED);
        answer.setAnswer(requestDto.getAnswer());
        return answer;
    }
}
