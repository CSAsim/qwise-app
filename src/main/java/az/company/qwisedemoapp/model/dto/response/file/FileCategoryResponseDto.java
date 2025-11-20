package az.company.qwisedemoapp.model.dto.response.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileCategoryResponseDto {

    private Long id;

    private String name;

    private List<FileSubcategoryResponseDto> subCategories;
}
