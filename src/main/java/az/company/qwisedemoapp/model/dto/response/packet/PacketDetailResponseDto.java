package az.company.qwisedemoapp.model.dto.response.packet;

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
public class PacketDetailResponseDto {

    private Long id;

    private String authorName;

    private String description;

    private String category;

    private String subCategory;

    private Integer totalQuestionCount;

    private Float price;

    private String thumbnailUrl;

    private PacketStatus status;

    private LocalDateTime createdAt;

    private List<QuestionResponseDto> questions;
}