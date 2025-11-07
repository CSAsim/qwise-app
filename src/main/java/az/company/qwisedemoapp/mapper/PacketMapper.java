package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.Packet;
import az.company.qwisedemoapp.model.dto.request.UpdatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.PacketDetailResponseDto;
import az.company.qwisedemoapp.model.dto.request.CreatePacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.PacketListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {QuestionMapper.class, UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PacketMapper {

    PacketMapper INSTANCE = Mappers.getMapper(PacketMapper.class);

    @Mapping(target = "authorName", source = "authorName")
    @Mapping(target = "totalQuestionCount", ignore = true)
    PacketListResponseDto toDto(Packet entity);

    @Mapping(target = "authorName", source = "authorName")
    @Mapping(target = "totalQuestionCount", ignore = true)
    PacketDetailResponseDto toDtoDetail(Packet entity);

    List<PacketDetailResponseDto> toDtoList(List<Packet> entities);

    default Page<PacketListResponseDto> toDtoPage(Page<Packet> entities) {
        return entities.map(this::toDto);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "enrolledStudents", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    Packet toEntity(CreatePacketRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "enrolledStudents", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    Packet toEntity(UpdatePacketRequestDto request, @MappingTarget Packet entity);
}
