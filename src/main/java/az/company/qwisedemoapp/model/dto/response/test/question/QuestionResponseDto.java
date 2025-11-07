package az.company.qwisedemoapp.model.dto.response.test.question;

import az.company.qwisedemoapp.model.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class QuestionResponseDto {

    private Long id;
    private Long questionNumber;
    private QuestionType questionType;
    private String questionText;
    private String questionImage;
}
