package az.company.qwisedemoapp.controller.packet;

import az.company.qwisedemoapp.model.dto.response.packet.PacketDetailResponseDto;
import az.company.qwisedemoapp.model.dto.response.packet.PacketListResponseDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.service.packet.PacketService;
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
@RequestMapping("/api/v1/qwise-app/packets")
public class PacketController {

    private final PacketService packetService;

    @PostMapping("/all")
    public ResponseEntity<PageableResponseDto<PacketListResponseDto>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestBody FilteredRequestDto request
            ) {
        Page<PacketListResponseDto> page = packetService.findAllPackets(request, pageable);
        PageableResponseDto<PacketListResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PacketDetailResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(packetService.findById(id));
    }
}
