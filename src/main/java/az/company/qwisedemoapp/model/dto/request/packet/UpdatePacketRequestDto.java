package az.company.qwisedemoapp.model.dto.request.packet;

import az.company.qwisedemoapp.model.dto.request.test.question.QuestionRequestDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePacketRequestDto {

    private String name;

    private String authorName;

    private Long subCategoryId;

    private Long categoryId;

    private String description;

    private Float rating;

    private Float price;

    private String thumbnailUrl;

    private List<QuestionRequestDto> questions;

    private PacketStatus status;
}
