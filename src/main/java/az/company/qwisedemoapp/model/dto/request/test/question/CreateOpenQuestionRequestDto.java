package az.company.qwisedemoapp.model.dto.request.test.question;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOpenQuestionRequestDto {

    @NotBlank
    private String inputFormat;

    @NotBlank
    private String answer;
}
