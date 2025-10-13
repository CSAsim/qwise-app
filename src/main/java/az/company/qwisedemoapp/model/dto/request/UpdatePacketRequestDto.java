package az.company.qwisedemoapp.model.dto.request;

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

    private String subCategory;

    private String description;

    private String category;

    private Float rating;

    private Float price;

    private String thumbnailUrl;

    private List<QuestionRequestDto> questions;

    private PacketStatus status;
}
