package az.company.qwisedemoapp.model.dto.response.test.question;

import az.company.qwisedemoapp.model.dto.OptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosedQuestionResponseDto {

    List<OptionDto> options;
}
