package az.company.qwisedemoapp.domain.entity.test.question;

import az.company.qwisedemoapp.domain.entity.BaseEntity;
import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.domain.entity.test.answer.UserAnswer;
import az.company.qwisedemoapp.model.enums.TestType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question extends BaseEntity {

    @Column(name = "question_number", nullable = false)
    private Long questionNumber;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TestType type;

    @Column(name = "question", nullable = false)
    private String questionText;

    @Column(name = "question_image")
    private String questionImage;

    @Column(name = "hint_text")
    private String hintText;

    @ManyToOne
    @JoinColumn(name = "packet_id", nullable = false)
    private Packet packet;

    @ToString.Exclude
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserAnswer> userAnswers;
}
