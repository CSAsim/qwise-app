package az.company.qwisedemoapp.model.dto.response.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacketCategoryResponseDto {

    private Long id;

    private String name;

    private List<PacketSubcategoryResponseDto> subCategories;
}
