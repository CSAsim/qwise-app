package az.company.qwisedemoapp.service.packet;

import az.company.qwisedemoapp.domain.entity.packet.PacketCategory;
import az.company.qwisedemoapp.domain.entity.packet.PacketSubcategory;
import az.company.qwisedemoapp.domain.repository.packet.PacketCategoryRepository;
import az.company.qwisedemoapp.domain.repository.packet.PacketSubcategoryRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.request.packet.PacketSubcategoryRequest;
import az.company.qwisedemoapp.model.dto.response.packet.PacketSubcategoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PacketSubcategoryService {

    private final PacketSubcategoryRepository packetSubCategoryRepository;
    private final PacketCategoryRepository packetCategoryRepository;

    public List<PacketSubcategoryResponseDto> findAllSubCategoriesByCategoryId(Long categoryId) {
        List<PacketSubcategory> subCategories = packetSubCategoryRepository
                .findAllByCategoryIdOrderByNameAsc(categoryId);
        return subCategories.stream()
                .map(subCategory -> {
                    PacketSubcategoryResponseDto dto = new PacketSubcategoryResponseDto();
                    dto.setId(subCategory.getId());
                    dto.setName(subCategory.getName());
                    return dto;
                })
                .toList();
    }

    @Transactional
    public List<PacketSubcategoryResponseDto> createSubCategories(
            Long categoryId,
            List<PacketSubcategoryRequest> requests) {
        PacketCategory category = packetCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        List<PacketSubcategoryResponseDto> responses = new ArrayList<>();
        for (PacketSubcategoryRequest subCategoryRequest : requests) {
            if (packetSubCategoryRepository.existsByNameIgnoreCaseAndCategoryId(
                    subCategoryRequest.getName(),
                    categoryId)) {
                continue;
            }
            PacketSubcategory packetSubCategory = PacketSubcategory.builder()
                    .name(subCategoryRequest.getName())
                    .category(category)
                    .build();
            PacketSubcategory saved = packetSubCategoryRepository.save(packetSubCategory);
            category.addSubCategory(packetSubCategory);
            log.info("Sub category created: {}", saved);
            responses.add(PacketSubcategoryResponseDto.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .build());
        }
        return responses;
    }

    @Transactional
    public void deleteSubCategory(List<Long> ids) {
        List<PacketSubcategory> subCategories = packetSubCategoryRepository.findAllById(ids);
        if (subCategories.size() != ids.size()) {
            throw new NotFoundException("One or more sub categories not found");
        }
        for (PacketSubcategory subCategory : subCategories) {
            subCategory.getCategory().removeSubCategory(subCategory);
        }
        packetSubCategoryRepository.deleteAll(subCategories);
        log.info("Sub categories deleted");
    }
}
