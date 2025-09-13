package az.company.qwisedemoapp.mapper;

import az.company.qwisedemoapp.domain.entity.User;
import az.company.qwisedemoapp.model.dto.UserResponse;
import az.company.qwisedemoapp.model.request.RegisterUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse toResponse(User entity);

    List<UserResponse> toResponseList(List<User> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "otpCodes", ignore = true)
    @Mapping(target = "passwordResetToken", ignore = true)
    @Mapping(target = "packets", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterUserRequest request);

    default Page<UserResponse> toResponsePage(Page<User> entities) {
        return entities.map(this::toResponse);
    }

    User toEntity(UserResponse userResponse);
}
