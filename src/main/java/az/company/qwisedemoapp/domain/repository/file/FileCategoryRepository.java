package az.company.qwisedemoapp.domain.repository.file;

import az.company.qwisedemoapp.domain.entity.file.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileCategoryRepository extends JpaRepository<FileCategory, Long> {

    List<FileCategory> findAllByOrderByNameAsc();

    boolean existsByNameIgnoreCase(String name);
}
