package az.company.qwisedemoapp.model.dto.request;

import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserPacketRequestDto {

    private Float progress;

    private PacketUsageStatus usageStatus;
}
