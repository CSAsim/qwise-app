package az.company.qwisedemoapp.model.dto.response.test.answer;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InterviewAnswerResponseDto extends AnswerResponse {

    private Boolean answer;
}
