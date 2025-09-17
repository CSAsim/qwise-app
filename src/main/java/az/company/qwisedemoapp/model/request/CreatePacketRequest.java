package az.company.qwisedemoapp.model.request;

import az.company.qwisedemoapp.model.enums.PacketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePacketRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String subCategory;

    @NotNull
    private String description;

    @NotBlank
    private String category;

    @NotNull
    private Float price;

    @NotBlank
    private String thumbnailUrl;

    @NotNull
    private PacketStatus status;
}
