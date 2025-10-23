package az.company.qwisedemoapp.service.test;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.test.question.Question;
import az.company.qwisedemoapp.domain.repository.test.QuestionRepository;
import az.company.qwisedemoapp.mapper.QuestionMapper;
import az.company.qwisedemoapp.model.dto.request.test.question.QuestionRequestDto;
import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public List<QuestionResponseDto> createQuestions(Packet packet, List<QuestionRequestDto> questions) {
        List<Question> existingQuestions = questionMapper.toEntityList(questions, packet);
        existingQuestions.forEach(packet::addQuestion);
        questionRepository.saveAll(existingQuestions);
        return questionMapper.toResponseList(existingQuestions);
    }

    public List<QuestionResponseDto> updateQuestions(Packet packet, List<QuestionRequestDto> questions) {
        if (questions != null && !questions.isEmpty()) {
            List<Question> newQuestions = questionMapper.toEntityList(questions, packet);
            List<QuestionResponseDto> questionResponses = questionMapper.toResponseList(newQuestions);
            newQuestions.forEach(q -> q.setPacket(packet));
            questionRepository.saveAll(newQuestions);
            packet.setQuestions(newQuestions);
            return questionResponses;
        }
        return null;
    }
}
