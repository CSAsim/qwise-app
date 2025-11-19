package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.File;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, JpaSpecificationExecutor<File>{

    @Query(value = "SELECT f FROM File f WHERE f.status != 'DELETED' AND f.id = :id")
    Optional<File> findByIdWithStatus(@Param("id") Long id);

    @Query("""
        SELECT f FROM File f
        WHERE LOWER(f.name) LIKE %:q%
           OR LOWER(f.description) LIKE %:q%
           OR LOWER(f.category) LIKE %:q%
           OR LOWER(f.subCategory) LIKE %:q%
           OR LOWER(f.authorName) LIKE %:q%
        """)
    Page<File> search(@Param("q") String q, Pageable pageable);
    boolean existsById(@NotNull Long id);
}
