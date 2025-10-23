package az.company.qwisedemoapp.domain.entity.test.answer;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.domain.entity.test.question.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_answer")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserAnswer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_packet_id")
    private UserPacket userPacket;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private UserPacketAttempt attempt;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;
}
