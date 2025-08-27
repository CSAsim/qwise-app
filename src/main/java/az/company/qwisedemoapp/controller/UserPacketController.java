package az.company.qwisedemoapp.controller;

import az.company.qwisedemoapp.model.dto.PageableResponseDto;
import az.company.qwisedemoapp.model.dto.UserPacketDto;
import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import az.company.qwisedemoapp.model.request.AssignPacketRequest;
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
@RequestMapping("/api/v1/user/packets")
public class UserPacketController {


    private final UserPacketService userPacketService;

    @GetMapping("/all")
    public ResponseEntity<PageableResponseDto<UserPacketDto>> getAll(
            @RequestParam PacketUsageStatus status,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<UserPacketDto> page = userPacketService.findAllUserPackets(status, pageable);
        PageableResponseDto<UserPacketDto> responseDto = PageableResponseDto.of(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/assign-to-course")
    public ResponseEntity<UserPacketDto> assignToCourse(@RequestBody AssignPacketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userPacketService.assignPacketToUser(request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userPacketService.deleteUserPacket(id);
        return ResponseEntity.noContent().build();
    }
}
