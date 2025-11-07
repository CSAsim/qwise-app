package az.company.qwisedemoapp.model.dto.request.test;


import az.company.qwisedemoapp.model.dto.request.test.answer.UserAnswerRequestDto;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptRequestDto {

    private Long attemptId;

    private List<UserAnswerRequestDto> answers;
}
