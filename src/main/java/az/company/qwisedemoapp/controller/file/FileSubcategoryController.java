package az.company.qwisedemoapp.controller.file;

import az.company.qwisedemoapp.model.dto.request.file.FileSubcategoryRequestDto;
import az.company.qwisedemoapp.model.dto.response.file.FileSubcategoryResponseDto;
import az.company.qwisedemoapp.service.file.FileSubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/file-subcategory")
public class FileSubcategoryController {

    private final FileSubcategoryService fileSubCategoryService;

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<List<FileSubcategoryResponseDto>> getAllByCategoryId(
            @PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(fileSubCategoryService.findAllSubCategoriesByCategoryId(categoryId));
    }
}
