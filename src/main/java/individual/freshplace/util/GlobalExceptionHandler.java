package individual.freshplace.util;

import individual.freshplace.dto.error.ErrorResponse;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity<ErrorResponse> duplicationExceptionHandler(final DuplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(value = WrongValueException.class)
    public ResponseEntity<ErrorResponse> wrongValueExceptionHandler(final WrongValueException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(StringLockException.class)
    public ResponseEntity<ErrorResponse> StringLockExceptionHandler(final StringLockException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ErrorResponse> AuthenticationExceptionHandler(final CustomAuthenticationException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.nothingValue(e.getErrorCode()));
    }

    @ExceptionHandler(NonExistentException.class)
    public ResponseEntity<ErrorResponse> NonExistentExceptionHandler(final NonExistentException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponse> EmptyFileExceptionHandler(final EmptyFileException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("validation error");
        return ResponseEntity.status(status).body(ErrorResponse.validation(ErrorCode.VALIDATION_ERROR, ex.getBindingResult().getAllErrors().toString()));
    }
}
