package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.model.dto.PacketDto;
import az.company.qwisedemoapp.model.request.CreatePacketRequest;
import az.company.qwisedemoapp.model.request.UpdatePacketRequest;
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
    PacketDto toDto(Packet entity);

    List<PacketDto> toDtoList(List<Packet> entities);

    default Page<PacketDto> toDtoPage(Page<Packet> entities) {
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
    Packet toEntity(CreatePacketRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "enrolledStudents", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Packet toEntity(UpdatePacketRequest request, @MappingTarget Packet entity);

    Packet toEntity(PacketDto packetDto);
}
