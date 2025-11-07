package az.company.qwisedemoapp.model.dto.response.attempt;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PausedAttemptResponseDto extends ResultAttemptResponseDto {

    private Integer answeredQuestionCount;

    private Integer unansweredQuestionCount;
}
