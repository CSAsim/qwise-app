package az.company.qwisedemoapp.domain.entity.test.question;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "open_questions")
public class OpenQuestion extends Question {

    @Column(name = "input_format", nullable = false)
    private String inputFormat;

    @Column(name = "answer")
    private String answer;
}
