package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.Contact;
import az.company.qwisedemoapp.model.dto.response.ContactResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface ContactMapper {

    ContactMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ContactMapper.class);

    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    ContactResponseDto toDto(Contact entity);

    default Page<ContactResponseDto> toDtoPage(Page<Contact> entities) {
        return entities.map(this::toDto);
    }
}
