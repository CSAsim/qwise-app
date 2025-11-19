package az.company.qwisedemoapp.domain.repository.packet;

import az.company.qwisedemoapp.domain.entity.packet.PacketSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacketSubcategoryRepository extends JpaRepository<PacketSubcategory, Long> {

    List<PacketSubcategory> findAllByCategoryIdOrderByNameAsc(Long categoryId);
    boolean existsByNameIgnoreCaseAndCategoryId(String name, Long categoryId);
}
