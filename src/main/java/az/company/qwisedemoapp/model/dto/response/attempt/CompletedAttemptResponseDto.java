package az.company.qwisedemoapp.model.dto.response.attempt;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CompletedAttemptResponseDto extends ResultAttemptResponseDto {

    private Integer correctAnswerCount;

    private Integer wrongAnswerCount;

    private Integer skippedAnswerCount;
}
