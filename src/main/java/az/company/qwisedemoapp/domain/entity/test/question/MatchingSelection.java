package az.company.qwisedemoapp.domain.entity.test.question;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "matching_selections")
public class MatchingSelection extends BaseEntity {

    private String leftKey;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "matching_selection_values", joinColumns = @JoinColumn(name = "selection_id"))
    @Column(name = "right_value")
    private List<String> chosenRightKeys = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "question_id")
    private MatchingQuestion question;
}
