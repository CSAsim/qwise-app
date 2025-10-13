package az.company.qwisedemoapp.model.dto.response.test.question;

import az.company.qwisedemoapp.model.dto.MatchingPairDto;
import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingQuestionResponseDto {

    private List<MatchingPairDto> matchingPairs;

    private List<MatchingSelectionDto> matchingSelections;
}
