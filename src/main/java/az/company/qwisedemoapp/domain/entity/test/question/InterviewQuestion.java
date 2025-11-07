package az.company.qwisedemoapp.domain.entity.test.question;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@Table(name = "interview_questions")
public class InterviewQuestion extends Question {

}
