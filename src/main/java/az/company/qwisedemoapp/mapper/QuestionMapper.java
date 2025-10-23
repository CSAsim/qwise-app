package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.test.Option;
import az.company.qwisedemoapp.domain.entity.test.question.MatchingSelection;
import az.company.qwisedemoapp.domain.entity.test.question.*;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.model.dto.MatchingListDto;
import az.company.qwisedemoapp.model.dto.MatchingVariantDto;
import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import az.company.qwisedemoapp.model.dto.OptionDto;
import az.company.qwisedemoapp.model.dto.request.test.question.QuestionRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.question.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final PacketRepository packetRepository;

    public Question toEntity(QuestionRequestDto request, Packet packet) {
        return switch (request.getQuestionType()) {
            case CLOSED -> buildClosedQuestion(request, packet);
            case OPEN -> buildOpenQuestion(request, packet);
            case MATCHING_QUESTION -> buildMatchingQuestion(request, packet);
        };
    }

    public List<Question> toEntityList(List<QuestionRequestDto> requestList, Packet packet) {
        return requestList.stream()
                .map(request -> toEntity(request, packet))
                .toList();
    }

    public void toEntity(Question existing, QuestionRequestDto request) {
        existing.setQuestionNumber(request.getQuestionNumber());
        existing.setQuestionText(request.getQuestionText());
        existing.setQuestionImage(request.getQuestionImage());
        existing.setHintText(request.getHintText());
        existing.setDescription(request.getDescription());
        existing.setExplanationVideoUrl(request.getExplanationVideoUrl());
        existing.setScore(request.getScore());

        switch (existing) {
            case ClosedQuestion closed when request.getOptions() != null -> {
                closed.getOptions().clear();
                List<Option> updatedOptions = request.getOptions()
                        .stream()
                        .map(o -> {
                            Option opt = new Option();
                            opt.setText(o.getText());
                            opt.setCorrect(o.isCorrect());
                            opt.setQuestion(closed);
                            return opt;
                        })
                        .toList();
                closed.setOptions(updatedOptions);
            }
            case OpenQuestion open -> {
                open.setAnswer(request.getAnswer());
                open.setInputFormat(request.getInputFormat());
            }
            case MatchingQuestion matching -> {
                matching.getMatchingLists().clear();
                matching.setMatchingLists(request.getMatchingLists()
                        .stream()
                        .map(l -> {
                            MatchingList list = new MatchingList();
                            list.setText(l.getText());
                            list.setQuestion(matching);
                            return list;
                        })
                        .toList());

                matching.getMatchingVariants().clear();
                matching.setMatchingVariants(request.getMatchingVariants()
                        .stream()
                        .map(p -> {
                            MatchingVariant pair = new MatchingVariant();
                            pair.setLeftItem(p.getLeftItem());
                            pair.setRightItem(p.getRightItem());
                            pair.setQuestion(matching);
                            return pair;
                        })
                        .toList());

                matching.getCorrectSelections().clear();
                matching.setCorrectSelections(request.getCorrectSelections()
                        .stream()
                        .map(s -> {
                            MatchingSelection sel = new MatchingSelection();
                            sel.setLeftKey(s.getLeftKey());
                            sel.setChosenRightKeys(s.getChosenRightKeys());
                            sel.setQuestion(matching);
                            return sel;
                        })
                        .toList());
            }
            default -> throw new IllegalStateException("Unexpected value: " + existing.getClass());
        }
    }

    public QuestionResponseDto toResponse(Question entity) {
        if (entity instanceof ClosedQuestion closed) {
            ClosedQuestionResponseDto closedQuestionResponseDto = new ClosedQuestionResponseDto();
            fillQuestionResponse(closedQuestionResponseDto, entity);
            closedQuestionResponseDto.setOptions(closed.getOptions()
                    .stream()
                    .map(o -> new OptionDto(o.getId(), o.getText(), o.isCorrect()))
                    .toList()
            );
            return closedQuestionResponseDto;
        }
        if (entity instanceof OpenQuestion open) {
            OpenQuestionResponseDto openQuestion = new OpenQuestionResponseDto();
            fillQuestionResponse(openQuestion, entity);
            openQuestion.setAnswer(open.getAnswer());
            openQuestion.setInputFormat(open.getInputFormat());
            return openQuestion;
        }

        if (entity instanceof MatchingQuestion matching) {
            MatchingQuestionResponseDtoD matchingQuestion = new MatchingQuestionResponseDtoD();
            fillQuestionResponse(matchingQuestion, entity);
            matchingQuestion.setMatchingLists(matching.getMatchingLists()
                    .stream()
                    .map(l -> {
                        MatchingListDto dto = new MatchingListDto();
                        dto.setId(l.getId());
                        dto.setText(l.getText());
                        return dto;
                    }).toList()
            );
            matchingQuestion.setMatchingVariants(matching.getMatchingVariants()
                    .stream()
                    .map(v -> {
                        MatchingVariantDto dto = new MatchingVariantDto();
                        dto.setId(v.getId());
                        dto.setLeftItem(v.getLeftItem());
                        dto.setRightItem(v.getRightItem());
                        return dto;
                    })
                    .toList()
            );
            matchingQuestion.setMatchingSelections(matching.getCorrectSelections()
                    .stream()
                    .map(s -> {
                        MatchingSelectionDto dto = new MatchingSelectionDto();
                        dto.setLeftKey(s.getLeftKey());
                        dto.setChosenRightKeys(s.getChosenRightKeys());
                        return dto;
                    })
                    .toList()
            );
            return matchingQuestion;
        }
        throw new IllegalArgumentException("Unsupported question type: " + entity.getClass());
    }

    public List<QuestionResponseDto> toResponseList(List<Question> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    private ClosedQuestion buildClosedQuestion(QuestionRequestDto request, Packet packet) {
        ClosedQuestion q = new ClosedQuestion();
        fillQuestionData(request, packet, q);
        List<Option> options = request.getOptions()
                .stream()
                .map(t -> {
                    Option option = new Option();
                    option.setText(t.getText());
                    option.setCorrect(t.isCorrect());
                    option.setQuestion(q);
                    return option;
                }).toList();
        q.setOptions(options);
        return q;
    }

    private OpenQuestion buildOpenQuestion(QuestionRequestDto request, Packet packet) {
        OpenQuestion q = new OpenQuestion();
        fillQuestionData(request, packet, q);
        q.setAnswer(request.getAnswer());
        q.setInputFormat(request.getInputFormat());
        return q;
    }

    private MatchingQuestion buildMatchingQuestion(QuestionRequestDto request, Packet packet) {
        MatchingQuestion q = new MatchingQuestion();
        fillQuestionData(request, packet, q);

        List<MatchingList> matchingLists = request.getMatchingLists()
                .stream()
                .map(l -> {
                    MatchingList list = new MatchingList();
                    list.setText(l.getText());
                    list.setQuestion(q);
                    return list;
                }).toList();
        q.setMatchingLists(matchingLists);

        List<MatchingVariant> matchingVariants = request.getMatchingVariants()
                .stream()
                .map(p -> {
                    MatchingVariant pair = new MatchingVariant();
                    pair.setLeftItem(p.getLeftItem());
                    pair.setRightItem(p.getRightItem());
                    pair.setQuestion(q);
                    return pair;
                }).toList();
        q.setMatchingVariants(matchingVariants);

        List<MatchingSelection> selections = request.getCorrectSelections()
                .stream()
                .map(s -> {
                    MatchingSelection sel = new MatchingSelection();
                    sel.setLeftKey(s.getLeftKey());
                    sel.setChosenRightKeys(s.getChosenRightKeys());
                    sel.setQuestion(q);
                    return sel;
                }).toList();
        q.setCorrectSelections(selections);

        return q;
    }

    private void fillQuestionData(QuestionRequestDto request, Packet packet, Question q) {
        q.setQuestionNumber(request.getQuestionNumber());
        q.setType(request.getQuestionType());
        q.setQuestionText(request.getQuestionText());
        q.setQuestionImage(request.getQuestionImage());
        q.setHintText(request.getHintText());
        q.setPacket(packet);
        q.setDescription(request.getDescription());
        q.setExplanationVideoUrl(request.getExplanationVideoUrl());
        q.setScore(request.getScore());
    }

    private void fillQuestionResponse(QuestionResponseDto response, Question entity) {
        response.setId(entity.getId());
        response.setQuestionNumber(entity.getQuestionNumber());
        response.setQuestionType(entity.getType());
        response.setQuestionText(entity.getQuestionText());
        response.setQuestionImage(entity.getQuestionImage());
        response.setHintText(entity.getHintText());
        response.setScore(entity.getScore());
    }
}
