package az.company.qwisedemoapp.model.dto.response.attempt;

import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ResultAttemptResponseDto {

    private Long id;

    private String authorName;

    private String description;

    private String category;

    private String subCategory;

    private Integer totalQuestionCount;

    private Float price;

    private String thumbnailUrl;

    private LocalDateTime createdAt;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private String duration;

    private List<AnswerResponse> result;

    private AttemptStatus status;
}
