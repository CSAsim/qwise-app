package az.company.qwisedemoapp.model.dto;

import az.company.qwisedemoapp.model.enums.PacketStatus;
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
public class UserPacketDto {

    private Long id;

    private PacketDto packet;

    private Long userId;

    private Float progress;

    private PacketUsageStatus usageStatus;

    private LocalDateTime enrolledAt;
}
