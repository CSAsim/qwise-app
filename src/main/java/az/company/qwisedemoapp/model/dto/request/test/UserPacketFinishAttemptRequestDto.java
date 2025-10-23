package az.company.qwisedemoapp.model.dto.request.test;


import az.company.qwisedemoapp.model.dto.request.test.answer.UserAnswerRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPacketFinishAttemptRequestDto {

    private Long attemptId;

    List<UserAnswerRequestDto> answers;
}
