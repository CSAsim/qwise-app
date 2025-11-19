package az.company.qwisedemoapp.model.dto.response.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacketSubcategoryResponseDto {

    private Long id;

    private String name;
}
