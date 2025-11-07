package az.company.qwisedemoapp.util;

import az.company.qwisedemoapp.model.dto.response.test.answer.AnswerResponse;
import az.company.qwisedemoapp.model.enums.AnswerStatus;

import java.util.List;

public class MathUtil {

    public static Integer calculateTotalCorrectQuestions(List<AnswerResponse> responses) {
        return (int) responses.stream().filter(a -> a.getStatus() == AnswerStatus.CORRECT).count();
    }

    public static Integer calculateTotalSkippedQuestions(List<AnswerResponse> responses) {
        return (int) responses.stream().filter(a -> a.getStatus() == AnswerStatus.SKIPPED).count();
    }

    public static Integer calculateTotalIncorrectQuestions(List<AnswerResponse> responses) {
        return (int) responses.stream().filter(a -> a.getStatus() == AnswerStatus.WRONG).count();
    }

    public static float calculateProgress(List<AnswerResponse> responses) {
        if (responses == null || responses.isEmpty()) {
            return 0.0f;
        }
        int totalAnswered = calculateTotalCorrectQuestions(responses) + calculateTotalIncorrectQuestions(responses);
        return (totalAnswered * 100.0f) / responses.size();
    }
}
