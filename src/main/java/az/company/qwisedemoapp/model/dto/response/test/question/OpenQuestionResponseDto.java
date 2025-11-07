package az.company.qwisedemoapp.model.dto.response.test.question;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OpenQuestionResponseDto extends QuestionResponseDto {

    private String hintText;
    private String description;
    private String explanationVideoUrl;

    private String inputFormat;

    private String answer;
}
