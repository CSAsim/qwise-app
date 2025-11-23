package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.UserPacket;
import az.company.qwisedemoapp.model.dto.request.AssignPacketRequestDto;
import az.company.qwisedemoapp.model.dto.response.attempt.UserPacketResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {QuestionMapper.class},
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface UserPacketMapper {

    UserPacketMapper INSTANCE = Mappers.getMapper(UserPacketMapper.class);

    @Mapping(target = "authorName", source = "resource.authorName")
    @Mapping(target = "name", source = "resource.name")
    @Mapping(target = "category", source = "resource.category.name")
    @Mapping(target = "subCategory", source = "resource.subcategory.name")
    @Mapping(target = "thumbnailUrl", source = "resource.thumbnailUrl")
    UserPacketResponseDto toDto(UserPacket entity);

    List<UserPacketResponseDto> toDtoList(List<UserPacket> entities);

    default Page<UserPacketResponseDto> toDtoPage(Page<UserPacket> entities) {
        return entities.map(this::toDto);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "attempts", ignore = true)
    UserPacket toEntity(AssignPacketRequestDto request);
}
