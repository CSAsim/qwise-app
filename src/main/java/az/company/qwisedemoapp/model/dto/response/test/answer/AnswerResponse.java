package az.company.qwisedemoapp.model.dto.response.test.answer;

import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import az.company.qwisedemoapp.model.enums.AnswerStatus;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "questionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenAnswerResponseDto.class, name = "OPEN"),
        @JsonSubTypes.Type(value = ClosedAnswerResponseDto.class, name = "CLOSED"),
        @JsonSubTypes.Type(value = MatchingAnswerResponseDto.class, name = "MATCHING")
})
public abstract class AnswerResponse {

    private Long id;

    private QuestionResponseDto question;

    private AnswerStatus status;

    private Integer score;
}
