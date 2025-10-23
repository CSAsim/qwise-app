package az.company.qwisedemoapp.model.dto.request.test.answer;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClosedAnswerRequestDto extends UserAnswerRequestDto{

    private Long optionId;
}
