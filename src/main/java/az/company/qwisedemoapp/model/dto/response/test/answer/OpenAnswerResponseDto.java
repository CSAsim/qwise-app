package az.company.qwisedemoapp.model.dto.response.test.answer;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OpenAnswerResponseDto extends AnswerResponse {

    private String yourAnswer;

    private String correctAnswer;
}
