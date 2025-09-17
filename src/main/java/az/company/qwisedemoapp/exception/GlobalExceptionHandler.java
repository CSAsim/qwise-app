package az.company.qwisedemoapp.exception;

import az.company.qwisedemoapp.model.constants.ErrorCode;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleNotFoundException(NotFoundException e) {
        return buildResponse(ErrorCode.NOT_FOUND, e.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<GlobalErrorResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return buildResponse(ErrorCode.ALREADY_EXISTS, e.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return buildResponse(ErrorCode.INVALID_INPUT, "Validation failed", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GlobalErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream()
                .map(this::formatViolation)
                .toList();

        return buildResponse(ErrorCode.BAD_REQUEST, "Validation failed", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalErrorResponse> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String typeName = Optional.ofNullable(e.getRequiredType())
                .map(Class::getSimpleName)
                .orElse("unknown");

        String message = String.format("Parameter '%s' should be of type %s",
                e.getName(), e.getRequiredType() != null ? typeName : "unknown");

        return buildResponse(ErrorCode.BAD_REQUEST, message, HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<GlobalErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        String message = String.format("Missing request header: %s", e.getHeaderName());
        return buildResponse(ErrorCode.BAD_REQUEST, message, HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<GlobalErrorResponse> handleMultipartException(MultipartException e) {
        String message = "PacketFile upload error";
        List<String> errors = Collections.singletonList(e.getMessage());

        return buildResponse(ErrorCode.BAD_REQUEST, message, HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorResponse> handleException(Exception e) {
        e.printStackTrace();

        return buildResponse(ErrorCode.BAD_REQUEST, null, HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<GlobalErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException e) {
        return buildResponse(
                "UNSUPPORTED_JWT",
                "The provided JWT token is not supported.",
                HttpStatus.UNAUTHORIZED,
                List.of(e.getMessage())
        );
    }

    private ResponseEntity<GlobalErrorResponse> buildResponse(String code, String message, HttpStatus status, List<String> errors) {
        return ResponseEntity.status(status)
                .body(GlobalErrorResponse.builder()
                        .requestId(UUID.randomUUID())
                        .errorCode(code)
                        .errorMessage(message)
                        .errors(errors)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private String formatViolation(ConstraintViolation<?> violation) {
        return violation.getPropertyPath() + ": " + violation.getMessage();
    }
}
