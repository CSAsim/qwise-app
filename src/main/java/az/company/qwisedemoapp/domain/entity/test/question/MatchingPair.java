package az.company.qwisedemoapp.domain.entity.test.question;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import jakarta.persistence.Column;
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
@Table(name = "matching_pair")
public class MatchingPair extends BaseEntity {

    @Column(name = "left_item", nullable = false)
    private String leftItem;

    @Column(name = "right_item", nullable = false)
    private String rightItem;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private MatchingQuestion question;
}
