package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.FileResponseDto;
import az.company.qwisedemoapp.model.dto.PageableResponseDto;
import az.company.qwisedemoapp.model.request.FilteredRequest;
import az.company.qwisedemoapp.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/file")
public class FileController {

    private final FileService fileService;

    @GetMapping("/all")
    public ResponseEntity<PageableResponseDto<FileResponseDto>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestBody FilteredRequest request
    ) {
        Page<FileResponseDto> page = fileService.findAllFiles(request, pageable);
        PageableResponseDto<FileResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.findById(id));
    }
}
