package az.company.qwisedemoapp.model.dto.response.test.question;

import az.company.qwisedemoapp.model.dto.OptionDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClosedQuestionResponseDto extends QuestionResponseDto {

    private String hintText;

    private String description;

    private String explanationVideoUrl;

    List<OptionDto> options;
}
