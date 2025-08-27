package az.company.qwisedemoapp.model.request;

import az.company.qwisedemoapp.model.enums.PacketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePacketRequest {

    private String subCategory;

    private String description;

    private String category;

    private Float rating;

    private Float price;

    private String thumbnailUrl;

    private PacketStatus status;
}
