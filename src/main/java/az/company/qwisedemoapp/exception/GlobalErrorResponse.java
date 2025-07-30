package az.company.qwisedemoapp.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalErrorResponse {

    private UUID requestId;
    private String errorCode;
    private String errorMessage;
    private List<String> errors;
    private LocalDateTime timeStamp;
}
