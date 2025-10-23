package az.company.qwisedemoapp.domain.entity.test.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interview_questions")
public class InterviewQuestion extends Question {

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;
}
