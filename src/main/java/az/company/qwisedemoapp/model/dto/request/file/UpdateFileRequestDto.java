package az.company.qwisedemoapp.model.dto.request.file;

import az.company.qwisedemoapp.model.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFileRequestDto {

    private String name;

    private String authorName;

    private String subCategory;

    private String category;

    private String description;

    private Float price;

    private String thumbnailUrl;

    private String fileUrl;

    private FileStatus status;
}
