package az.company.qwisedemoapp.controller.packet;

import az.company.qwisedemoapp.model.dto.response.packet.PacketCategoryResponseDto;
import az.company.qwisedemoapp.service.packet.PacketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/packet-category")
public class PacketCategoryController {

    private final PacketCategoryService packetCategoryService;

    @GetMapping("/all")
    public ResponseEntity<List<PacketCategoryResponseDto>> getAll() {
        List<PacketCategoryResponseDto> responses = packetCategoryService.findAllCategories();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacketCategoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(packetCategoryService.findById(id));
    }
}
