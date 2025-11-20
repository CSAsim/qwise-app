package az.company.qwisedemoapp.domain.repository.file;

import az.company.qwisedemoapp.domain.entity.file.FileSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileSubcategoryRepository extends JpaRepository<FileSubcategory, Long> {

    List<FileSubcategory> findAllByCategoryIdOrderByNameAsc(Long categoryId);

    boolean existsByNameIgnoreCaseAndCategoryId(String name, Long categoryId);
}
