package az.company.qwisedemoapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDto {

    private Long id;

    private Long userRank;

    private String profilePictureUrl;

    private String fullName;

    private Integer score;
}
