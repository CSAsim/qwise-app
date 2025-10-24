package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.test.UserPacketAttempt;
import az.company.qwisedemoapp.model.dto.request.test.UserPacketAttemptResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface UserPacketAttemptMapper {

    UserPacketAttemptMapper INSTANCE = Mappers.getMapper(UserPacketAttemptMapper.class);

    @Mapping(target = "userPacketId", source = "userPacket.packet.id")
    @Mapping(target = "result", ignore = true)
    @Mapping(target = "duration", ignore = true)
    UserPacketAttemptResponseDto toResponseDto(UserPacketAttempt entity);

    List<UserPacketAttemptResponseDto> toResponseDtoList(List<UserPacketAttempt> entities);
}
