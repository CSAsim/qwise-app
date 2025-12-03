package az.company.qwisedemoapp.domain.entity.test;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.domain.entity.test.answer.MatchingUserSelection;
import az.company.qwisedemoapp.domain.entity.test.answer.UserAnswer;
import az.company.qwisedemoapp.model.enums.AttemptStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_packet_attempts")
public class UserPacketAttempt extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_packet_id")
    private UserPacket userPacket;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber = 0;

    @Column(name = "time_limit")
    private Long timeLimit;

    @Builder.Default
    @Column(name = "duration")
    private Long duration = 0L;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "last_resumed_at")
    private LocalDateTime lastResumedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Builder.Default
    @Column(name = "total_correct_answer_count")
    private Integer totalCorrectAnswerCount = 0;

    @Builder.Default
    @Column(name = "total_wrong_answer_count", nullable = false)
    private Integer totalWrongAnswerCount = 0;

    @Builder.Default
    @Column(name = "total_skipped_answer_count", nullable = false)
    private Integer totalSkippedAnswerCount = 0;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AttemptStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAnswer> answers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MatchingUserSelection> userSelections = new ArrayList<>();


    public void addAnswer(UserAnswer answer) {
        answers.add(answer);
        answer.setAttempt(this);
    }

    public void removeAnswer(UserAnswer answer) {
        answers.remove(answer);
        answer.setAttempt(null);
    }

    public void addUserSelection(MatchingUserSelection userSelection) {
        userSelections.add(userSelection);
        userSelection.setAttempt(this);
    }

    public void removeUserSelection(MatchingUserSelection userSelection) {
        userSelections.remove(userSelection);
        userSelection.setAttempt(null);
    }
}
