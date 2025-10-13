package az.company.qwisedemoapp.model.dto.response.test.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenQuestionResponseDto {

    private String inputFormat;

    private String answer;
}
