package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.model.dto.response.attempt.CompletedAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.response.attempt.PausedAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.response.attempt.ResultAttemptResponseDto;
import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import az.company.qwisedemoapp.model.enums.AnswerStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPacketAttemptMapper {

    public ResultAttemptResponseDto toDto(UserPacketAttempt attempt,
                                          List<AnswerResponse> answerResponse) {

        return switch (attempt.getStatus()) {
            case PAUSED -> buildPausedAttemptResponse(attempt, answerResponse);
            case COMPLETED -> buildCompletedAttemptResponse(attempt, answerResponse);
            default -> throw new IllegalStateException("Unexpected value: " + attempt.getStatus());
        };
    }

    private PausedAttemptResponseDto buildPausedAttemptResponse(UserPacketAttempt attempt, List<AnswerResponse> answerResponse) {
        PausedAttemptResponseDto dto = new PausedAttemptResponseDto();
        fillAttemptResponse(dto, answerResponse, attempt);
        dto.setAnsweredQuestionCount((int)answerResponse
                .stream()
                .filter(obj ->
                        obj.getStatus().equals(AnswerStatus.CORRECT) ||
                                obj.getStatus().equals(AnswerStatus.WRONG))
                .count());
        dto.setUnansweredQuestionCount(answerResponse.size() - dto.getAnsweredQuestionCount());
        return dto;
    }

    private CompletedAttemptResponseDto buildCompletedAttemptResponse(UserPacketAttempt attempt,
                                                                      List<AnswerResponse> answerResponse) {
        CompletedAttemptResponseDto dto = new CompletedAttemptResponseDto();
        fillAttemptResponse(dto, answerResponse, attempt);
        dto.setCorrectAnswerCount(attempt.getTotalCorrectAnswerCount());
        dto.setSkippedAnswerCount(attempt.getTotalSkippedAnswerCount());
        dto.setWrongAnswerCount(attempt.getTotalWrongAnswerCount());
        dto.setTotalQuestionCount(
                attempt.getTotalCorrectAnswerCount() +
                        attempt.getTotalSkippedAnswerCount() +
                        attempt.getTotalWrongAnswerCount());
        return dto;
    }

    private void fillAttemptResponse(ResultAttemptResponseDto resultResponse,
                                     List<AnswerResponse> answerResponse,
                                     UserPacketAttempt attempt) {
        resultResponse.setId(attempt.getId());
        resultResponse.setAuthorName(attempt.getUser().getFullName());
        resultResponse.setDescription(attempt.getUserPacket().getResource().getDescription());
        resultResponse.setCategory(attempt.getUserPacket().getResource().getCategory().getName());
        resultResponse.setSubCategory(attempt.getUserPacket().getResource().getSubcategory().getName());
        resultResponse.setThumbnailUrl(attempt.getUserPacket().getResource().getThumbnailUrl());
        resultResponse.setPrice(attempt.getUserPacket().getResource().getPrice());
        resultResponse.setTotalQuestionCount(answerResponse.size());
        resultResponse.setCreatedAt(attempt.getCreatedAt());
        resultResponse.setStartedAt(attempt.getStartedAt());
        resultResponse.setFinishedAt(attempt.getFinishedAt());
        resultResponse.setStatus(attempt.getStatus());
        resultResponse.setDuration(formatDuration(attempt.getDuration()));
        resultResponse.setResult(answerResponse);
    }

    public String formatDuration(Long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        if (hours > 0 && minutes > 0) {
            return String.format("%d saat %d dəqiqə", hours, minutes);
        } else if (hours > 0) {
            return String.format("%d saat", hours);
        } else {
            return String.format("%d dəqiqə", minutes);
        }
    }
}
