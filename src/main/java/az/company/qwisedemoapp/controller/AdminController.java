package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.FileResponseDto;
import az.company.qwisedemoapp.model.dto.PacketResponseDto;
import az.company.qwisedemoapp.model.dto.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.UserResponseDto;
import az.company.qwisedemoapp.model.request.CreateFileRequest;
import az.company.qwisedemoapp.model.request.CreatePacketRequest;
import az.company.qwisedemoapp.model.request.UpdateFileRequest;
import az.company.qwisedemoapp.model.request.UpdatePacketRequest;
import az.company.qwisedemoapp.service.FileService;
import az.company.qwisedemoapp.service.PacketService;
import az.company.qwisedemoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/qwise-app/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PacketService packetService;
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<PageableResponseDto<UserResponseDto>> getAll(@PageableDefault(size = 10) Pageable pageable) {
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
    public ResponseEntity<PacketResponseDto> create(@Valid @RequestBody CreatePacketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packetService.createPacket(request));
    }

    @PostMapping("/packet/publish-packet/{id}")
    public ResponseEntity<PacketResponseDto> publish(@PathVariable Long id) {
        return ResponseEntity.ok(packetService.publishPacket(id));
    }

    @PutMapping("/packet/update-packet/{id}")
    public ResponseEntity<PacketResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePacketRequest request
    ) {
        return ResponseEntity.ok(packetService.updatePacket(id, request));
    }

    @DeleteMapping("/packet/delete-packet/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        packetService.deletePacket(id);
        return ResponseEntity.noContent().build();
    }

    //File operations
    @PostMapping("/file/new-file")
    public ResponseEntity<FileResponseDto> createFile(@Valid @RequestBody CreateFileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fileService.createFile(request));
    }

    @PostMapping("/file/publish-file/{fileId}")
    public ResponseEntity<FileResponseDto> publishFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(fileService.publishFile(fileId));
    }

    @PostMapping("/file/update-file/{id}")
    public ResponseEntity<FileResponseDto> updateFile(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateFileRequest request) {
        return ResponseEntity.ok(fileService.updateFile(id, request));
    }

    @DeleteMapping("/file/delete-file/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}
