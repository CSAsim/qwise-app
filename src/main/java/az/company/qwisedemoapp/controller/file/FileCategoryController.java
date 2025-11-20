package az.company.qwisedemoapp.controller.file;

import az.company.qwisedemoapp.model.dto.response.file.FileCategoryResponseDto;
import az.company.qwisedemoapp.service.file.FileCategoryService;
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
@RequestMapping("/api/v1/qwise-app/file-category")
public class FileCategoryController {

    private final FileCategoryService fileCategoryService;

    @GetMapping("/all")
    public ResponseEntity<List<FileCategoryResponseDto>> getAll() {
        List<FileCategoryResponseDto> responses = fileCategoryService.findAllCategories();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileCategoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fileCategoryService.findById(id));
    }
}
