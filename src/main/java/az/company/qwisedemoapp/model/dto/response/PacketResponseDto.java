package az.company.qwisedemoapp.model.dto.response;

import az.company.qwisedemoapp.model.dto.response.test.question.QuestionResponseDto;
import az.company.qwisedemoapp.model.enums.PacketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacketResponseDto {

    private Long id;

    private Long authorId;

    private String authorName;

    private String name;

    private String description;

    private String category;

    private String subCategory;

    private Integer totalQuestionCount;

    private Float price;

    private String thumbnailUrl;

    private List<QuestionResponseDto> questions;

    private PacketStatus status;

    private LocalDateTime createdAt;
}