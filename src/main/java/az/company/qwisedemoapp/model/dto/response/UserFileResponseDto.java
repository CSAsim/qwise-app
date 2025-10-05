package az.company.qwisedemoapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFileResponseDto {

    private Long id;

    private Long studentId;

    private FileResponseDto file;
}
