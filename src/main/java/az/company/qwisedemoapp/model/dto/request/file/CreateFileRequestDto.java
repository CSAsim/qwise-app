package az.company.qwisedemoapp.model.dto.request.file;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String authorName;

    @NotNull
    private Long subcategoryId;

    @NotNull
    private Long categoryId;

    private String description;

    private Float price;

    @NotBlank
    private String thumbnailUrl;

    @NotBlank
    private String fileUrl;
}