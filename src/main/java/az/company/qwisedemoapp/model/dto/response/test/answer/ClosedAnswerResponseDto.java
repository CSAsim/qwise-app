package az.company.qwisedemoapp.model.dto.response.test.answer;

import az.company.qwisedemoapp.model.dto.OptionDto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClosedAnswerResponseDto extends AnswerResponse {

    private OptionDto yourOption;

    private OptionDto correctOption;
}
