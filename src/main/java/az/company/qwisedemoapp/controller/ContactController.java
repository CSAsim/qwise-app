package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.ContactRequestDto;
import az.company.qwisedemoapp.model.dto.response.ContactResponseDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.service.ContactService;
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
@RequestMapping("/api/v1/qwise-app/contact")
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/all/{id}")
    public ResponseEntity<PageableResponseDto<ContactResponseDto>> getAll(
            @PathVariable("id") Long userId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ContactResponseDto> page = contactService.findAllByUserId(userId, pageable);
        PageableResponseDto<ContactResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ContactResponseDto> create(@RequestBody ContactRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.save(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ContactResponseDto> update(@PathVariable Long id, @RequestBody ContactRequestDto request) {
        return ResponseEntity.ok(contactService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
