package az.company.qwisedemoapp.domain.repository.test;

import az.company.qwisedemoapp.domain.entity.test.question.MatchingSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingSelectionRepository extends JpaRepository<MatchingSelection, Long> {

    Optional<MatchingSelection> findByQuestionId(Long questionId);

    boolean existsByQuestionId(Long questionId);
}
