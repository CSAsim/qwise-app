package az.company.qwisedemoapp.model.dto.request.test.answer;

import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MatchingAnswerRequestDto extends UserAnswerRequestDto {

    private List<MatchingSelectionDto> userSelections;
}
