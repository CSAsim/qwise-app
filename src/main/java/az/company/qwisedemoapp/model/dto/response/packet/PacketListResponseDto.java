package az.company.qwisedemoapp.model.dto.response.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacketListResponseDto {

    private Long id;

    private String name;

    private String authorName;

    private String category;

    private String subCategory;

    private String status;

    private String thumbnailUrl;

    private Integer totalQuestionCount;
}