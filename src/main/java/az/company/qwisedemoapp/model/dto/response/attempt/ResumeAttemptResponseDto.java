package az.company.qwisedemoapp.model.dto.response.attempt;

import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAttemptResponseDto {

    private Long attemptId;

    private String packetName;

    private List<AnswerResponse> lastModifiedAnswers;
}
