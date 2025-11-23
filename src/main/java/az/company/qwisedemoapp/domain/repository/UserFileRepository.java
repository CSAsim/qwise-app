package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    boolean existsByUserIdAndResourceId(Long studentId, Long fileId);
    Page<UserFile> findAll(Specification<UserFile> specification, Pageable sortedPageable);
}
