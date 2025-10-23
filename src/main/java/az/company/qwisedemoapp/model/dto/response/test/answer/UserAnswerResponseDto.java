package az.company.qwisedemoapp.model.dto.response.test.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerResponseDto {

    private Long userPacketId;

    private List<AnswerResponse> answers;
}
