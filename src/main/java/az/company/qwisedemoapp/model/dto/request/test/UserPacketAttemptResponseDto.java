package az.company.qwisedemoapp.model.dto.request.test;

import az.company.qwisedemoapp.model.dto.response.test.answer.UserAnswerResponseDto;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPacketAttemptResponseDto {

    private Long id;
    private Long userPacketId;
    private Integer attemptNumber;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String duration;
    private Integer totalCorrectAnswerCount;
    private Integer totalWrongAnswerCount;
    private Integer totalSkippedAnswerCount;
    private Float totalScore;
    private UserAnswerResponseDto result;
    private AttemptStatus status;
}
