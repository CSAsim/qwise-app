package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.test.MatchingSelection;
import az.company.qwisedemoapp.domain.entity.test.question.*;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.MatchingPairDto;
import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import az.company.qwisedemoapp.model.dto.OptionDto;
import az.company.qwisedemoapp.model.dto.request.test.question.QuestionRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final PacketRepository packetRepository;

    public Question toEntity(QuestionRequestDto request) {
        Packet packet = packetRepository.findById(request.getPacketId())
                .orElseThrow(() -> new NotFoundException("Packet not found"));

        return switch (request.getTestType()) {
            case CLOSED -> buildClosedQuestion(request, packet);
            case OPEN -> buildOpenQuestion(request, packet);
            case MATCHING_QUESTION -> buildMatchingQuestion(request, packet);
        };
    }

    public List<Question> toEntityList(List<QuestionRequestDto> requestList) {
        return requestList.stream()
                .map(this::toEntity)
                .toList();
    }

    public void toEntity(Question existing, QuestionRequestDto request) {
        existing.setQuestionText(request.getQuestionText());
        existing.setQuestionImage(request.getQuestionImage());
        existing.setHintText(request.getHintText());

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
                matching.getPairs().clear();
                matching.setPairs(request.getMatchingPairs()
                        .stream()
                        .map(p -> {
                            MatchingPair pair = new MatchingPair();
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
        QuestionResponseDto response = new QuestionResponseDto();
        response.setId(entity.getId());
        response.setQuestionNumber(entity.getQuestionNumber());
        response.setTestType(entity.getType());
        response.setQuestionText(entity.getQuestionText());
        response.setQuestionImage(entity.getQuestionImage());
        response.setHintText(entity.getHintText());
        response.setPacketId(entity.getPacket().getId());

        switch (entity) {
            case ClosedQuestion closed -> response.setOptions(
                    closed.getOptions().stream()
                            .map(o -> {
                                OptionDto dto = new OptionDto();
                                dto.setId(o.getId());
                                dto.setText(o.getText());
                                dto.setCorrect(o.isCorrect());
                                return dto;
                            })
                            .toList()
            );
            case OpenQuestion open -> {
                response.setInputFormat(open.getInputFormat());
                response.setAnswer(open.getAnswer());
            }
            case MatchingQuestion matching -> {
                response.setMatchingPairs(
                        matching.getPairs().stream()
                                .map(p -> {
                                    MatchingPairDto dto = new MatchingPairDto();
                                    dto.setId(p.getId());
                                    dto.setLeftItem(p.getLeftItem());
                                    dto.setRightItem(p.getRightItem());
                                    return dto;
                                })
                                .toList()
                );
                response.setCorrectSelections(
                        matching.getCorrectSelections().stream()
                                .map(s -> new MatchingSelectionDto(s.getLeftKey(), s.getChosenRightKeys()))
                                .toList()
                );
            }
            default -> throw new IllegalStateException("Unexpected value: " + entity.getClass());
        }

        return response;
    }

    public List<QuestionResponseDto> toResponseList(List<Question> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    private ClosedQuestion buildClosedQuestion(QuestionRequestDto request, Packet packet) {
        ClosedQuestion q = new ClosedQuestion();
        q.setQuestionNumber(request.getQuestionNumber());
        q.setType(request.getTestType());
        q.setQuestionText(request.getQuestionText());
        q.setQuestionImage(request.getQuestionImage());
        q.setHintText(request.getHintText());
        q.setPacket(packet);

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
        q.setQuestionNumber(request.getQuestionNumber());
        q.setType(request.getTestType());
        q.setQuestionText(request.getQuestionText());
        q.setQuestionImage(request.getQuestionImage());
        q.setHintText(request.getHintText());
        q.setPacket(packet);
        q.setAnswer(request.getAnswer());
        q.setInputFormat(request.getInputFormat());
        return q;
    }

    private MatchingQuestion buildMatchingQuestion(QuestionRequestDto request, Packet packet) {
        MatchingQuestion q = new MatchingQuestion();
        q.setQuestionNumber(request.getQuestionNumber());
        q.setType(request.getTestType());
        q.setQuestionText(request.getQuestionText());
        q.setQuestionImage(request.getQuestionImage());
        q.setHintText(request.getHintText());
        q.setPacket(packet);

        List<MatchingPair> pairs = request.getMatchingPairs()
                .stream()
                .map(p -> {
                    MatchingPair pair = new MatchingPair();
                    pair.setLeftItem(p.getLeftItem());
                    pair.setRightItem(p.getRightItem());
                    pair.setQuestion(q);
                    return pair;
                }).toList();
        q.setPairs(pairs);

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
}
