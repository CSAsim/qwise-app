package az.company.qwisedemoapp.model.dto.request.test.answer;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InterviewAnswerRequestDto extends UserAnswerRequestDto {

    @NotNull
    private Boolean answer;
}
