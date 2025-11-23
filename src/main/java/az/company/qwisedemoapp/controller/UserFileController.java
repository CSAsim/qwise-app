package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.response.UserFileResponseDto;
import az.company.qwisedemoapp.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/user/files")
public class UserFileController {

    private final UserFileService userFileService;

    @PostMapping("/all")
    public ResponseEntity<PageableResponseDto<UserFileResponseDto>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestBody FilteredRequestDto request) {
        Page<UserFileResponseDto> page = userFileService.findAllUserFiles(request, pageable);
        PageableResponseDto<UserFileResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/assign-to-file")
    public ResponseEntity<UserFileResponseDto> assignToFIle(@RequestParam Long fileId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userFileService.assignFileToStudent(fileId));
    }

    @DeleteMapping("/remove-file/{fileId}")
    public ResponseEntity<Void> removeFile(@PathVariable Long fileId) {
        userFileService.removeFileFromStudent(fileId);
        return ResponseEntity.noContent().build();
    }
}
