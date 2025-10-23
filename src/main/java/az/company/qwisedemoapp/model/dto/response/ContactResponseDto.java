package az.company.qwisedemoapp.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponseDto {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String text;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
