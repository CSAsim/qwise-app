package az.company.qwisedemoapp.domain.entity.test.answer;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.entity.test.question.MatchingQuestion;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "matching_user_selections")
public class MatchingUserSelection extends BaseEntity {

    private String leftKey;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "matching_user_selection_values", joinColumns = @JoinColumn(name = "selection_id"))
    @Column(name = "right_value")
    private List<String> userSelections = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private MatchingAnswer answer;

    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private UserPacketAttempt attempt;

    @ManyToOne
    @JoinColumn(name = "mathcing_question_id")
    private MatchingQuestion question;
}
