package az.company.qwisedemoapp.domain.entity.test.question;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "matching_lists")
public class MatchingList extends BaseEntity {

    private String text;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private MatchingQuestion question;
}
