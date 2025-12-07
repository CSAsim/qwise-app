package az.company.qwisedemoapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderBoardResponseDto {

    private PageableResponseDto<RatingResponseDto> ratings;

    private Long myRank;

    private RatingResponseDto myRating;
}
