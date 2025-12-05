package az.company.qwisedemoapp.model.dto.request;

import az.company.qwisedemoapp.model.enums.RatingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequestDto {

    private RatingType type;

    private LocalDateTime periodStart;
}
