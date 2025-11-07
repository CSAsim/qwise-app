package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.request.AssignPacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.response.attempt.UserPacketResponseDto;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import az.company.qwisedemoapp.service.UserPacketService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/user/packets")
public class UserPacketController {


    private final UserPacketService userPacketService;

    @GetMapping("/all")
    public ResponseEntity<PageableResponseDto<UserPacketResponseDto>> getAll(
            @RequestParam PacketUsageStatus status,
            @RequestParam(required = false) Long studentId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<UserPacketResponseDto> page = userPacketService.findAllUserPackets(status, studentId, pageable);
        PageableResponseDto<UserPacketResponseDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/assign-to-course")
    public ResponseEntity<UserPacketResponseDto> assignToCourse(@RequestBody AssignPacketRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userPacketService.assignPacketToUser(request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long packetId) {
        userPacketService.deleteUserPacket(packetId);
        return ResponseEntity.noContent().build();
    }
}
