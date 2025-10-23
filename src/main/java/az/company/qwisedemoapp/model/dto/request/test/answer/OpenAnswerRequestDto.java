package az.company.qwisedemoapp.model.dto.request.test.answer;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OpenAnswerRequestDto extends UserAnswerRequestDto {

    private String yourAnswer;
}
