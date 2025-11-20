package az.company.qwisedemoapp.model.dto.request.packet;

import az.company.qwisedemoapp.model.dto.request.test.question.QuestionRequestDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import az.company.qwisedemoapp.model.enums.PacketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePacketRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String authorName;

    @NotNull
    private Long subCategoryId;

    @NotNull
    private Long categoryId;

    @NotNull
    private String description;

    @NotNull
    private Float price;

    @NotBlank
    private String thumbnailUrl;

    private List<QuestionRequestDto> questions;

    @NotNull
    private PacketStatus status;

    @NotNull
    private PacketType type;
}
