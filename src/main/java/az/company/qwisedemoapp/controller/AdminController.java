package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.packet.CreatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.request.UpdateFileRequestDto;
import az.company.qwisedemoapp.model.dto.request.packet.PacketCategoryRequest;
import az.company.qwisedemoapp.model.dto.request.packet.PacketSubcategoryRequest;
import az.company.qwisedemoapp.model.dto.request.packet.UpdatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.packet.PacketCategoryResponseDto;
import az.company.qwisedemoapp.model.dto.response.packet.PacketDetailResponseDto;
import az.company.qwisedemoapp.model.dto.response.FileResponseDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.response.UserResponseDto;
import az.company.qwisedemoapp.model.dto.request.CreateFileRequestDto;
import az.company.qwisedemoapp.model.dto.response.packet.PacketSubcategoryResponseDto;
import az.company.qwisedemoapp.service.FileService;
import az.company.qwisedemoapp.service.packet.PacketCategoryService;
import az.company.qwisedemoapp.service.packet.PacketService;
import az.company.qwisedemoapp.service.UserService;
import az.company.qwisedemoapp.service.packet.PacketSubcategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/qwise-app/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PacketService packetService;
    private final FileService fileService;
    private final PacketCategoryService packetCategoryService;
    private final PacketSubcategoryService packetSubCategoryService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<UserResponseDto>> getAllUsers(@PageableDefault(size = 10) Pageable pageable) {
        Page<UserResponseDto> page = userService.findAll(pageable);
        PageableResponseDto<UserResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    //Packet operations

    @PostMapping("/packet/new-packet")
    public ResponseEntity<PacketDetailResponseDto> create(@Valid @RequestBody CreatePacketRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packetService.createPacket(request));
    }

    @PostMapping("/packet/publish-packet/{id}")
    public ResponseEntity<PacketDetailResponseDto> publish(@PathVariable Long id) {
        return ResponseEntity.ok(packetService.publishPacket(id));
    }

    @PutMapping("/packet/update-packet/{id}")
    public ResponseEntity<PacketDetailResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePacketRequestDto request
    ) {
        return ResponseEntity.ok(packetService.updatePacket(id, request));
    }

    @DeleteMapping("/packet/delete-packet/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        packetService.deletePacket(id);
        return ResponseEntity.noContent().build();
    }

    //Packet Category operations

    @PostMapping("/packet-category/create")
    public ResponseEntity<PacketCategoryResponseDto> createCategory(
            @Valid @RequestBody PacketCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(packetCategoryService.createCategory(request));
    }

    @DeleteMapping("/packet-category/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        packetCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    //Packet Subcategory operations

    @PostMapping("/packet-subcategory/create")
    public ResponseEntity<List<PacketSubcategoryResponseDto>> createSubCategory(
            @RequestParam Long categoryId,
            @Valid @RequestBody List<PacketSubcategoryRequest> requests) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(packetSubCategoryService.createSubCategories(categoryId, requests));
    }

    @DeleteMapping("/packet-subcategory/delete")
    public ResponseEntity<Void> deleteSubCategory(@RequestBody List<Long> ids) {
        packetSubCategoryService.deleteSubCategory(ids);
        return ResponseEntity.noContent().build();
    }

    //File operations

    @PostMapping("/file/new-file")
    public ResponseEntity<FileResponseDto> createFile(@Valid @RequestBody CreateFileRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fileService.createFile(request));
    }

    @PostMapping("/file/publish-file/{fileId}")
    public ResponseEntity<FileResponseDto> publishFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(fileService.publishFile(fileId));
    }

    @PostMapping("/file/update-file/{id}")
    public ResponseEntity<FileResponseDto> updateFile(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateFileRequestDto request) {
        return ResponseEntity.ok(fileService.updateFile(id, request));
    }

    @DeleteMapping("/file/delete-file/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}
