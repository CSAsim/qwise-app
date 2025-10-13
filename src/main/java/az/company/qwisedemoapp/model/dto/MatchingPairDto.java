package az.company.qwisedemoapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingPairDto {

    private Long id;

    @NotBlank
    private String leftItem;

    @NotBlank
    private String rightItem;
}
