package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.test.Option;
import az.company.qwisedemoapp.domain.entity.test.answer.*;
import az.company.qwisedemoapp.domain.entity.test.question.MatchingSelection;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.entity.test.question.*;
import az.company.qwisedemoapp.domain.repository.test.OptionRepository;
import az.company.qwisedemoapp.domain.repository.test.QuestionRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import az.company.qwisedemoapp.model.dto.OptionDto;
import az.company.qwisedemoapp.model.dto.request.test.answer.ClosedAnswerRequestDto;
import az.company.qwisedemoapp.model.dto.request.test.answer.MatchingAnswerRequestDto;
import az.company.qwisedemoapp.model.dto.request.test.answer.OpenAnswerRequestDto;
import az.company.qwisedemoapp.model.dto.request.test.answer.UserAnswerRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.answer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAnswerMapper {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public UserAnswer toEntity(UserPacketAttempt attempt, UserAnswerRequestDto dto) {

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new NotFoundException("Question not found"));
        return switch (dto.getQuestionType()) {
            case "OPEN" -> buildOpenAnswer(attempt, question, dto);
            case "CLOSED" -> buildClosedAnswer(attempt, question, dto);
            case "MATCHING_QUESTION" -> buildMatchingAnswer(attempt, question, dto);
            default -> throw new IllegalStateException("Unexpected value: " + dto.getQuestionType());
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
            OpenAnswerResponseDto dto = new OpenAnswerResponseDto();
            dto.setId(answer.getId());
            dto.setQuestionId(answer.getQuestion().getId());
            dto.setQuestionType(answer.getQuestion().getType());
            dto.setScore(open.isCorrect() ? answer.getQuestion().getScore() : 0.0f);
            dto.setYourAnswer(open.getAnswer());
            dto.setCorrectAnswer(((OpenQuestion) answer.getQuestion()).getAnswer());
            dto.setCorrect(open.isCorrect());
            return dto;
        }
        if (answer instanceof ClosedAnswer closed) {
            ClosedAnswerResponseDto dto = new ClosedAnswerResponseDto();
            dto.setId(answer.getId());
            dto.setQuestionId(answer.getQuestion().getId());
            dto.setQuestionType(answer.getQuestion().getType());
            dto.setScore(closed.isCorrect() ? answer.getQuestion().getScore() : 0.0f);
            OptionDto optionDto = OptionDto.builder()
                    .id(closed.getOption().getId())
                    .text(closed.getOption().getText())
                    .correct(closed.getOption().isCorrect())
                    .build();
            OptionDto correctOption = ((ClosedQuestion) answer.getQuestion()).getOptions()
                    .stream()
                    .filter(Option::isCorrect)
                    .findFirst()
                    .map(o -> new OptionDto(o.getId(), o.getText(), o.isCorrect()))
                    .orElse(null);
            dto.setCorrect(closed.isCorrect());
            dto.setYourOption(optionDto);
            dto.setCorrectOption(correctOption);
            return dto;
        }

        if (answer instanceof MatchingAnswer matching) {
            MatchingAnswerResponseDto dto = new MatchingAnswerResponseDto();
            dto.setId(answer.getId());
            dto.setQuestionId(answer.getQuestion().getId());
            dto.setQuestionType(answer.getQuestion().getType());
            dto.setScore(matching.isCorrect() ? answer.getQuestion().getScore() : 0.0f);

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
            dto.setCorrect(matching.isCorrect());
            return dto;
        }
        throw new IllegalArgumentException("Unsupported answer type: " + answer.getClass());
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
        OpenAnswer answer = new OpenAnswer();
        answer.setUserPacket(attempt.getUserPacket());
        answer.setQuestion(question);
        answer.setAttempt(attempt);
        answer.setAnswer(openDto.getYourAnswer());
        answer.setCorrect(openDto.getYourAnswer().equalsIgnoreCase(((OpenQuestion) question).getAnswer()));
        return answer;
    }

    private ClosedAnswer buildClosedAnswer(UserPacketAttempt attempt, Question question, UserAnswerRequestDto dto) {
        ClosedAnswerRequestDto closedDto = (ClosedAnswerRequestDto) dto;
        ClosedAnswer answer = new ClosedAnswer();
        answer.setUserPacket(attempt.getUserPacket());
        answer.setQuestion(question);
        answer.setAttempt(attempt);
        Option selectedOption = optionRepository.findById(closedDto.getOptionId())
                .orElseThrow(() -> new NotFoundException("Option not found"));
        answer.setOption(selectedOption);
        answer.setCorrect(selectedOption.isCorrect());
        return answer;
    }

    private MatchingAnswer buildMatchingAnswer(UserPacketAttempt attempt, Question question, UserAnswerRequestDto dto) {
        MatchingAnswerRequestDto matchingDto = (MatchingAnswerRequestDto) dto;
        MatchingAnswer answer = new MatchingAnswer();
        answer.setUserPacket(attempt.getUserPacket());
        answer.setQuestion(question);
        answer.setAttempt(attempt);

        List<MatchingUserSelection> selections = new ArrayList<>();
        for (MatchingSelectionDto s : matchingDto.getUserSelections()) {
            MatchingUserSelection userSelection = new MatchingUserSelection();
            userSelection.setQuestion((MatchingQuestion) question);
            userSelection.setAttempt(attempt);
            userSelection.setAnswer(answer);
            userSelection.setLeftKey(s.getLeftKey());
            userSelection.setUserSelections(new ArrayList<>(s.getChosenRightKeys())); // ✅ mutable
            selections.add(userSelection);
        }
        answer.setSelections(new ArrayList<>(selections)); // ✅ mutable

        List<MatchingSelection> correctSelections = ((MatchingQuestion) question).getCorrectSelections();
        boolean isCorrect = selections.size() == correctSelections.size() &&
                selections.stream().allMatch(s -> correctSelections.stream()
                        .anyMatch(cs -> cs.getLeftKey().equals(s.getLeftKey())
                                && new HashSet<>(cs.getChosenRightKeys())
                                .equals(new HashSet<>(s.getUserSelections()))));
        answer.setCorrect(isCorrect);
        return answer;
    }
}
