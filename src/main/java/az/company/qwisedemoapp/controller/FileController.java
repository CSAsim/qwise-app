package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.response.FileResponseDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/file")
public class FileController {

    private final FileService fileService;

    @PostMapping("/all/by-category")
    public ResponseEntity<PageableResponseDto<FileResponseDto>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestBody FilteredRequestDto request
    ) {
        Page<FileResponseDto> page = fileService.findAllFilesByCategory(request, pageable);
        PageableResponseDto<FileResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/all/by-filer")
    public ResponseEntity<PageableResponseDto<FileResponseDto>> getAllByFiler(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String sort
    ) {
        Page<FileResponseDto> page = fileService.findAllFilesBySearch(sort, pageable);
        PageableResponseDto<FileResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PageableResponseDto<FileResponseDto>> search(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<FileResponseDto> page = fileService.findAllFilesBySearch(search, pageable);
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
