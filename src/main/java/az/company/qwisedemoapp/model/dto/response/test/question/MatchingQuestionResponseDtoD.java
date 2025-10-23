package az.company.qwisedemoapp.model.dto.response.test.question;

import az.company.qwisedemoapp.model.dto.MatchingListDto;
import az.company.qwisedemoapp.model.dto.MatchingVariantDto;
import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MatchingQuestionResponseDtoD extends QuestionResponseDto {

    private List<MatchingListDto> matchingLists;

    private List<MatchingVariantDto> matchingVariants;

    private List<MatchingSelectionDto> matchingSelections;
}
