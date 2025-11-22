package az.company.qwisedemoapp.service.file;

import az.company.qwisedemoapp.domain.entity.file.FileCategory;
import az.company.qwisedemoapp.domain.entity.file.FileSubcategory;
import az.company.qwisedemoapp.domain.repository.file.FileCategoryRepository;
import az.company.qwisedemoapp.exception.AlreadyExistsException;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.request.file.FileCategoryRequestDto;
import az.company.qwisedemoapp.model.dto.response.file.FileCategoryResponseDto;
import az.company.qwisedemoapp.model.dto.response.file.FileSubcategoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileCategoryService {

    private final FileCategoryRepository fileCategoryRepository;

    public List<FileCategoryResponseDto> findAllCategories() {
        List<FileCategory> categories = fileCategoryRepository.findAllByOrderByNameAsc();
        List<FileSubcategoryResponseDto> subcategories = categories.stream()
                .flatMap(c -> c.getSubcategories()
                        .stream()
                        .map(s -> {
                            FileSubcategoryResponseDto dto = new FileSubcategoryResponseDto();
                            dto.setId(s.getId());
                            dto.setName(s.getName());
                            return dto;
                        })).toList();
        return categories.stream()
                .map(c -> {
                    FileCategoryResponseDto dto = new FileCategoryResponseDto();
                    dto.setId(c.getId());
                    dto.setName(c.getName());
                    dto.setSubCategories(subcategories);
                    return dto;
                }).toList();
    }

    public FileCategoryResponseDto findById(Long id) {
        FileCategory category = fileCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        List<FileSubcategoryResponseDto> subcategories = category.getSubcategories()
                .stream()
                .map(s -> {
                    FileSubcategoryResponseDto dto = new FileSubcategoryResponseDto();
                    dto.setId(s.getId());
                    dto.setName(s.getName());
                    return dto;
                }).toList();
        FileCategoryResponseDto dto = new FileCategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSubCategories(subcategories);
        return dto;
    }

    @Transactional
    public FileCategoryResponseDto createFileCategory(FileCategoryRequestDto request) {
        log.info("Creating category: {}", request);
        boolean exists = fileCategoryRepository.existsByNameIgnoreCase(request.getName());
        if (exists) {
            throw new AlreadyExistsException("Category already exists");
        }
        FileCategory category = FileCategory.builder().name(request.getName()).build();
        FileCategory saved = fileCategoryRepository.save(category);
        log.info("Category created: {}", saved);
        return FileCategoryResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category with id {}", id);
        fileCategoryRepository.deleteById(id);
    }
}
