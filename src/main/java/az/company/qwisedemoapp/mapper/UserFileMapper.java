package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.UserFile;
import az.company.qwisedemoapp.model.dto.response.UserFileResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, FileMapper.class},
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface UserFileMapper {

    UserFileMapper INSTANCE = Mappers.getMapper(UserFileMapper.class);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "file", source = "file")
    UserFileResponseDto toDto(UserFile entity);

    default Page<UserFileResponseDto> toDtoPage(Page<UserFile> entities) {
        return entities.map(this::toDto);
    }
}
