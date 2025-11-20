package az.company.qwisedemoapp.model.dto.response.file;

import az.company.qwisedemoapp.model.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {

    private Long id;

    private Long authorId;

    private String authorName;

    private String name;

    private String subcategory;

    private String category;

    private String description;

    private Float rating;

    private Float price;

    private String thumbnailUrl;

    private String fileUrl;

    private FileStatus status;

    private LocalDateTime createdAt;
}
