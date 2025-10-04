package az.company.qwisedemoapp.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFileRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String subCategory;

    @NotBlank
    private String category;

    private String description;

    private Float price;

    @NotBlank
    private String thumbnailUrl;

    @NotBlank
    private String fileUrl;
}