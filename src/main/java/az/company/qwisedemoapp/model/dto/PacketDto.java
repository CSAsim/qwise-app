package az.company.qwisedemoapp.model.dto;

import az.company.qwisedemoapp.model.enums.PacketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacketDto {

    private Long id;

    private String subCategory;

    private Long authorId;

    private String authorName;

    private String description;

    private String category;

    private Float rating;

    private Float price;

    private String thumbnailUrl;

    private PacketStatus status;

    private LocalDateTime createdAt;
}