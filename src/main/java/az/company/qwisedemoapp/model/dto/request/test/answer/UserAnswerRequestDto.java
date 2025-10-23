package az.company.qwisedemoapp.model.dto.request.test.answer;

import az.company.qwisedemoapp.model.enums.QuestionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenAnswerRequestDto.class, name = "OPEN"),
        @JsonSubTypes.Type(value = ClosedAnswerRequestDto.class, name = "CLOSED"),
        @JsonSubTypes.Type(value = MatchingAnswerRequestDto.class, name = "MATCHING_QUESTION")
})
public abstract class UserAnswerRequestDto {

    private Long questionId;

    private String questionType;
}

