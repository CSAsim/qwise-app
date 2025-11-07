package az.company.qwisedemoapp.domain.repository.test;

import az.company.qwisedemoapp.domain.entity.test.answer.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    Optional<UserAnswer> findByQuestionId(Long questionId);
}
