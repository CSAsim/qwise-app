    package az.company.qwisedemoapp.model.dto.response.test.answer;

    import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
    import lombok.*;

    import java.util.List;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public class MatchingAnswerResponseDto extends AnswerResponse {

        private List<MatchingSelectionDto> yourSelections;

        private List<MatchingSelectionDto> correctSelections;
    }
