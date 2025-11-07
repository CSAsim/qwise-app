package az.company.qwisedemoapp.domain.entity.test.answer;

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
@Table(name = "interview_answers")
public class InterviewAnswer extends UserAnswer {

    @Column(name = "is_correct", nullable = false)
    private Boolean answer;
}
