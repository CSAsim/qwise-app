package az.company.qwisedemoapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;

    private String profilePictureUrl;

    private String fullName;

    private String phoneNumber;

    private String email;
}
