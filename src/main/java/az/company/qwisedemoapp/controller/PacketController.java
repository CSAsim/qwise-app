package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.PacketDto;
import az.company.qwisedemoapp.model.dto.PageableResponseDto;
import az.company.qwisedemoapp.model.request.CreatePacketRequest;
import az.company.qwisedemoapp.model.request.FilteredPacketRequest;
import az.company.qwisedemoapp.model.request.UpdatePacketRequest;
import az.company.qwisedemoapp.service.PacketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/packets")
public class PacketController {

    private final PacketService packetService;

    @PostMapping("/all")
    public ResponseEntity<PageableResponseDto<PacketDto>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestBody FilteredPacketRequest request
            ) {
        Page<PacketDto> page = packetService.findAllPackets(request, pageable);
        PageableResponseDto<PacketDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacketDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(packetService.findById(id));
    }

    @PostMapping("/admin/new-packet")
    public ResponseEntity<PacketDto> create(@Valid @RequestBody CreatePacketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packetService.createPacket(request));
    }

    @PostMapping("/publish-packet/{id}")
    public ResponseEntity<PacketDto> publish(@PathVariable Long id) {
        return ResponseEntity.ok(packetService.publishPacket(id));
    }

    @PutMapping("/admin/update-packet/{id}")
    public ResponseEntity<PacketDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePacketRequest request
    ) {
        return ResponseEntity.ok(packetService.updatePacket(id, request));
    }

    @DeleteMapping("/admin/delete-packet/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        packetService.deletePacket(id);
        return ResponseEntity.noContent().build();
    }
}
