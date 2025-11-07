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

    @Transactional
    public void createQuestions(Packet packet, List<QuestionRequestDto> questions) {
        log.info("Creating questions: {}", questions.size());
        List<Question> existingQuestions = questionMapper.toEntityList(questions, packet);
        existingQuestions.forEach(packet::addQuestion);
        questionRepository.saveAll(existingQuestions);
        log.info("Questions created: {}", existingQuestions.size());
        questionMapper.toResponseList(existingQuestions);
    }

    @Transactional
    public List<QuestionResponseDto> updateQuestions(Packet packet, List<QuestionRequestDto> questions) {
        if (questions != null && !questions.isEmpty()) {
            log.info("Updating questions: {}", questions.size());
            List<Question> newQuestions = questionMapper.toEntityList(questions, packet);
            List<QuestionResponseDto> questionResponses = questionMapper.toResponseList(newQuestions);
            newQuestions.forEach(q -> q.setPacket(packet));
            questionRepository.saveAll(newQuestions);
            packet.setQuestions(newQuestions);
            log.info("Questions updated: {}", newQuestions.size());
            return questionResponses;
        }
        return null;
    }
}
