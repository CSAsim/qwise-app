package az.company.qwisedemoapp.model.dto.response.attempt;

import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartAttemptResponseDto {

    private Long userPacketId;

    private Long attemptId;

    private Integer attemptNumber;

    private String packetName;

    private List<QuestionResponseDto> questionResponse;
}
