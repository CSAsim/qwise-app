package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.File;
import az.company.qwisedemoapp.model.dto.FileResponseDto;
import az.company.qwisedemoapp.model.request.CreateFileRequest;
import az.company.qwisedemoapp.model.request.UpdateFileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorFullName", source = "author.fullName")
    FileResponseDto toDto(File file);

    List<FileResponseDto> toDtoList(List<File> entities);

    default Page<FileResponseDto> toDtoPage(Page<File> entities) {
        return entities.map(this::toDto);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    File toEntity(CreateFileRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    File toEntity(@MappingTarget File entity, UpdateFileRequest request);
}
