package az.company.qwisedemoapp.domain.entity.test.answer;

import az.company.qwisedemoapp.domain.entity.test.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "closed_answers")
public class ClosedAnswer extends UserAnswer {

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = false)
    private Option option;
}
