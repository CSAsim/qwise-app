package az.company.qwisedemoapp.controller.packet;

import az.company.qwisedemoapp.model.dto.response.packet.PacketSubcategoryResponseDto;
import az.company.qwisedemoapp.service.packet.PacketSubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qwise-app/packet-subcategory")
public class PacketSubcategoryController {

    private final PacketSubcategoryService packetSubCategoryService;

    @GetMapping("/all/{categoryId}")
    public ResponseEntity<List<PacketSubcategoryResponseDto>> getAllByCategoryId(
            @PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(packetSubCategoryService.
                findAllSubCategoriesByCategoryId(categoryId));
    }
}
