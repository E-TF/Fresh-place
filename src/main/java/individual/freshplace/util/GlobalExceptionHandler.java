package individual.freshplace.util;

import individual.freshplace.dto.error.ErrorResponse;
import individual.freshplace.dto.mail.MailRequest;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.constant.MailFormat;
import individual.freshplace.util.exception.*;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String RECEIVER =  "rnjstndus23@gmail.com";
    private final CustomMailSender customMailSender;

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ErrorResponse> duplicationExceptionHandler(final DuplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(WrongValueException.class)
    public ResponseEntity<ErrorResponse> wrongValueExceptionHandler(final WrongValueException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(StringLockException.class)
    public ResponseEntity<ErrorResponse> stringLockExceptionHandler(final StringLockException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationExceptionHandler(final CustomAuthenticationException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.nothingValue(e.getErrorCode()));
    }

    @ExceptionHandler(NonExistentException.class)
    public ResponseEntity<ErrorResponse> nonExistentExceptionHandler(final NonExistentException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponse> emptyFileExceptionHandler(final EmptyFileException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(OutOfInventoryException.class)
    public ResponseEntity<ErrorResponse> outOfInventoryExceptionHandler(final OutOfInventoryException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(UriException.class)
    public ResponseEntity<ErrorResponse> UriExceptionHandler(final UriException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    @ExceptionHandler(FileUploadFailedException.class)
    public ResponseEntity<ErrorResponse> FileUploadFailedExceptionHandler(final FileUploadFailedException e) {
        customMailSender.sendMail(MailRequest.builder()
                .address(RECEIVER)
                .title(MailFormat.IMAGE_UPLOAD_FAILED.getTitle())
                .content(createMailContent(e.getValue(), e.getErrorCode().getMessage())).build());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.containsValue(e.getErrorCode(), e.getValue()));
    }

    private String createMailContent(String imageName, String cause) {
        return imageName + "이 (" + cause + ")에 의해 업로드 실패 했습니다.";
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("validation error");
        return ResponseEntity.status(status).body(ErrorResponse.validation(ErrorCode.VALIDATION_ERROR, ex.getBindingResult().getAllErrors().toString()));
    }
}
