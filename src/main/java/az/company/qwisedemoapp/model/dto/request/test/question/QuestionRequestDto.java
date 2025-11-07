package az.company.qwisedemoapp.model.dto.request.test.question;

import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import az.company.qwisedemoapp.model.dto.OptionDto;
import az.company.qwisedemoapp.model.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {

    @NotBlank
    private Long questionNumber;

    @NotBlank
    private QuestionType questionType;

    @NotBlank
    private String questionText;

    private String questionImage;

    private String hintText;

    private String description;

    private String explanationVideoUrl;

    private Long packetId;

    //For closed question
    private List<OptionDto> options;

    //For open question
    private String inputFormat;

    private String answer;

    private List<MatchingSelectionDto> correctSelections;
}
