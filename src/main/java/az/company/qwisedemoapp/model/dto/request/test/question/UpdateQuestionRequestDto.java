package az.company.qwisedemoapp.model.dto.request.test.question;

import az.company.qwisedemoapp.model.dto.MatchingPairDto;
import az.company.qwisedemoapp.model.dto.MatchingSelectionDto;
import az.company.qwisedemoapp.model.dto.OptionDto;
import az.company.qwisedemoapp.model.enums.TestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionRequestDto {

    private Long questionNumber;

    private TestType testType;

    private String questionText;

    private String questionImage;

    private String hintText;

    private Long packetId;

    //For closed question
    private List<OptionDto> options;

    //For open question
    private String inputFormat;
    private String answer;

    //For matching question
    private List<MatchingPairDto> pairs;
    private List<MatchingSelectionDto> correctSelections;
}
