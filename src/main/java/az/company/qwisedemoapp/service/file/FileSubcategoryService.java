package az.company.qwisedemoapp.service.file;

import az.company.qwisedemoapp.domain.entity.file.FileCategory;
import az.company.qwisedemoapp.domain.entity.file.FileSubcategory;
import az.company.qwisedemoapp.domain.repository.file.FileCategoryRepository;
import az.company.qwisedemoapp.domain.repository.file.FileSubcategoryRepository;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.request.file.FileSubcategoryRequestDto;
import az.company.qwisedemoapp.model.dto.response.file.FileSubcategoryResponseDto;
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
public class FileSubcategoryService {

    private final FileSubcategoryRepository fileSubcategoryRepository;
    private final FileCategoryRepository fileCategoryRepository;

    public List<FileSubcategoryResponseDto> findAllSubCategoriesByCategoryId(Long categoryId) {
        List<FileSubcategory> subcategories = fileSubcategoryRepository
                .findAllByCategoryIdOrderByNameAsc(categoryId);
        return subcategories.stream()
                .map(subCategory -> {
                    FileSubcategoryResponseDto dto = new FileSubcategoryResponseDto();
                    dto.setId(subCategory.getId());
                    dto.setName(subCategory.getName());
                    return dto;
                })
                .toList();
    }

    @Transactional
    public List<FileSubcategoryResponseDto> createSubcategories(
            Long categoryId,
            List<FileSubcategoryRequestDto> requests) {
        log.info("Creating sub categories: {}", requests);
        FileCategory category = fileCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("File category not found"));
        List<FileSubcategoryResponseDto> subcategoryResponses = new ArrayList<>();
        for (FileSubcategoryRequestDto request : requests) {
            if (fileSubcategoryRepository.existsByNameIgnoreCaseAndCategoryId(
                    request.getName(),
                    categoryId)) {
                continue;
            }
            FileSubcategory subcategory = FileSubcategory.builder()
                    .name(request.getName())
                    .category(category)
                    .build();
            FileSubcategory saved = fileSubcategoryRepository.save(subcategory);
            category.addSubcategory(subcategory);
            subcategoryResponses.add(FileSubcategoryResponseDto.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .build());
        }
        log.info("Sub categories created");
        return subcategoryResponses;
    }

    @Transactional
    public void deleteSubcategory(List<Long> ids) {
        log.info("Deleting sub categories: {}", ids);
        List<FileSubcategory> subcategories = fileSubcategoryRepository.findAllById(ids);
        if (subcategories.size() != ids.size()) {
            throw new NotFoundException("One or more sub categories not found");
        }
        for (FileSubcategory subcategory : subcategories) {
            subcategory.getCategory().removeSubcategory(subcategory);
        }
        fileSubcategoryRepository.deleteAll(subcategories);
        log.info("Sub categories deleted");
    }
}
