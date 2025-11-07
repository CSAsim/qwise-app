package az.company.qwisedemoapp.model.dto.response.attempt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPacketResponseDto {

    private Long id;

    private String name;

    private String authorName;

    private String category;

    private String subCategory;

    private Float progress;

    private String thumbnailUrl;

    private Integer totalQuestionCount;
}
