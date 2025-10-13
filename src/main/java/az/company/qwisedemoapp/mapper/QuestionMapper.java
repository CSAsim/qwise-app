package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.test.MatchingSelection;
import az.company.qwisedemoapp.domain.entity.test.question.*;
import az.company.qwisedemoapp.domain.repository.PacketRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.request.test.question.CreateQuestionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final PacketRepository packetRepository;

    public Question toEntity(CreateQuestionRequestDto request) {
        Packet packet = packetRepository.findById(request.getPacketId())
                .orElseThrow(() -> new NotFoundException("Packet not found"));

        return switch (request.getTestType()) {
            case CLOSED -> buildClosedQuestion(request, packet);
            case OPEN -> buildOpenQuestion(request, packet);
            case MATCHING_QUESTION -> buildMatchingQuestion(request, packet);
        };
    }

    private ClosedQuestion buildClosedQuestion(CreateQuestionRequestDto request, Packet packet) {
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

    private OpenQuestion buildOpenQuestion(CreateQuestionRequestDto request, Packet packet) {
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

    private MatchingQuestion buildMatchingQuestion(CreateQuestionRequestDto request, Packet packet) {
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
