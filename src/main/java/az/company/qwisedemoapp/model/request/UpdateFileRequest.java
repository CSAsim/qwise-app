package az.company.qwisedemoapp.model.request;

import az.company.qwisedemoapp.model.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFileRequest {

    private String name;

    private String subCategory;

    private String category;

    private String description;

    private Float price;

    private String thumbnailUrl;

    private String fileUrl;

    private FileStatus status;
}
