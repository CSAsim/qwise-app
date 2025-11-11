package az.company.qwisedemoapp.domain.repository;

import az.company.qwisedemoapp.domain.entity.File;
import org.jetbrains.annotations.NotNull;
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
    boolean existsById(@NotNull Long id);
}
