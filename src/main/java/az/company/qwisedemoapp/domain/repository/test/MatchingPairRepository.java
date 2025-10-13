package az.company.qwisedemoapp.domain.repository.test;

import az.company.qwisedemoapp.domain.entity.test.question.MatchingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingPairRepository extends JpaRepository<MatchingPair, Long> {

    Optional<MatchingPair> findByQuestionId(Long questionId);

    boolean existsByQuestionId(Long questionId);
}
