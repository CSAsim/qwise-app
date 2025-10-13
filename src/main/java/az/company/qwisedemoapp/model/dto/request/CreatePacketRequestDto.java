package az.company.qwisedemoapp.model.dto.request;

import az.company.qwisedemoapp.model.dto.request.test.question.QuestionRequestDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
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
    private String subCategory;

    @NotNull
    private String description;

    @NotBlank
    private String category;

    @NotNull
    private Float price;

    @NotBlank
    private String thumbnailUrl;

    private List<QuestionRequestDto> questions;

    @NotNull
    private PacketStatus status;
}
