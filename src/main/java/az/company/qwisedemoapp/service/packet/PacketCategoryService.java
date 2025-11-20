package az.company.qwisedemoapp.service.packet;

import az.company.qwisedemoapp.domain.entity.packet.PacketCategory;
import az.company.qwisedemoapp.domain.repository.packet.PacketCategoryRepository;
import az.company.qwisedemoapp.exception.AlreadyExistsException;
import az.company.qwisedemoapp.exception.NotFoundException;
import az.company.qwisedemoapp.model.dto.request.packet.PacketCategoryRequest;
import az.company.qwisedemoapp.model.dto.response.packet.PacketCategoryResponseDto;
import az.company.qwisedemoapp.model.dto.response.packet.PacketSubcategoryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PacketCategoryService {

    private final PacketCategoryRepository packetCategoryRepository;

    public List<PacketCategoryResponseDto> findAllCategories() {
        List<PacketCategory> categories = packetCategoryRepository.findAllByOrderByNameAsc();
        return categories.stream()
                .map(category -> {
                    PacketCategoryResponseDto categoryResponseDto = new PacketCategoryResponseDto();
                    categoryResponseDto.setId(category.getId());
                    categoryResponseDto.setName(category.getName());
                    return categoryResponseDto;
                }).toList();
    }

    public PacketCategoryResponseDto findById(Long id) {
        PacketCategory category = packetCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        List<PacketSubcategoryResponseDto> subcategories = category.getSubCategories()
                .stream()
                .map(s -> {
                    PacketSubcategoryResponseDto dto = new PacketSubcategoryResponseDto();
                    dto.setId(s.getId());
                    dto.setName(s.getName());
                    return dto;
                }).toList();
        return PacketCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .subCategories(subcategories)
                .build();
    }

    @Transactional
    public PacketCategoryResponseDto createCategory(PacketCategoryRequest request) {
        log.info("Creating category: {}", request);
        boolean exists = packetCategoryRepository.existsByNameIgnoreCase(request.getName());
        if (exists) {
            throw new AlreadyExistsException("Category already exists");
        }
        PacketCategory category = PacketCategory.builder().name(request.getName()).build();
        PacketCategory saved = packetCategoryRepository.save(category);
        log.info("Category created: {}", saved);
        return PacketCategoryResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category with id {}", id);
        packetCategoryRepository.deleteById(id);
    }
}
