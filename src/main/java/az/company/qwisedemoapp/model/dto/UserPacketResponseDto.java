package az.company.qwisedemoapp.model.dto;

import az.company.qwisedemoapp.model.enums.PacketUsageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPacketResponseDto {

    private Long id;

    private PacketResponseDto packet;

    private Long userId;

    private Float progress;

    private PacketUsageStatus usageStatus;

    private LocalDateTime enrolledAt;
}
