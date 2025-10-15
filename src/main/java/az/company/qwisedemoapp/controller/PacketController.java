package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.response.PacketResponseDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.request.FilteredRequestDto;
import az.company.qwisedemoapp.service.PacketService;
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
@RequestMapping("/packets")
public class PacketController {

    private final PacketService packetService;

    @GetMapping("/all")
    public ResponseEntity<PageableResponseDto<PacketResponseDto>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestBody FilteredRequestDto request
            ) {
        Page<PacketResponseDto> page = packetService.findAllPackets(request, pageable);
        PageableResponseDto<PacketResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacketResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(packetService.findById(id));
    }
}
