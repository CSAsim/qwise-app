package az.company.qwisedemoapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingSelectionDto {

    @NotBlank
    private String leftKey;

    private List<String> chosenRightKeys;
}
