package az.company.qwisedemoapp.model.dto.request.packet;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacketSubcategoryRequest {

    @NotBlank
    private String name;
}
