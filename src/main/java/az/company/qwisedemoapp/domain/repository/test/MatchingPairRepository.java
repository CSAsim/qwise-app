package az.company.qwisedemoapp.domain.repository.test;

import az.company.qwisedemoapp.domain.entity.test.question.MatchingVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingPairRepository extends JpaRepository<MatchingVariant, Long> {

    Optional<MatchingVariant> findByQuestionId(Long questionId);

    boolean existsByQuestionId(Long questionId);
}
