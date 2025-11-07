package az.company.qwisedemoapp.model.dto.request.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartAttemptRequestDto {

    private Long userPacketId;
}
