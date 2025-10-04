package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.model.dto.request.UpdatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.PacketResponseDto;
import az.company.qwisedemoapp.model.dto.request.CreatePacketRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface PacketMapper {

    PacketMapper INSTANCE = Mappers.getMapper(PacketMapper.class);

    @Mapping(target = "authorName", source = "author.fullName")
    @Mapping(target = "authorId", source = "author.id")
    PacketResponseDto toDto(Packet entity);

    List<PacketResponseDto> toDtoList(List<Packet> entities);

    default Page<PacketResponseDto> toDtoPage(Page<Packet> entities) {
        return entities.map(this::toDto);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "enrolledStudents", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Packet toEntity(CreatePacketRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "enrolledStudents", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Packet toEntity(UpdatePacketRequestDto request, @MappingTarget Packet entity);

    Packet toEntity(PacketResponseDto packetDto);
}
