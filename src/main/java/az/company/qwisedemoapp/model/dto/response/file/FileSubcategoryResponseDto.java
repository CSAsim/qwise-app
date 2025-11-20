package az.company.qwisedemoapp.model.dto.response.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSubcategoryResponseDto {

    private Long id;

    private String name;
}
