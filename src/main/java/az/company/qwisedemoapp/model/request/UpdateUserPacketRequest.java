package az.company.qwisedemoapp.model.request;

import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserPacketRequest {

    private Float progress;

    private PacketUsageStatus usageStatus;
}
