package individual.freshplace.util;

import individual.freshplace.util.constant.ErrorCode;
import lombok.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime date;
    private int status;
    private String error;
    private String code;
    private String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.date = LocalDateTime.now();
        this.status = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.code = errorCode.name();
        this.message = message;
    }

    public static ResponseEntity<ErrorResponse> errorResponseAndValue(ErrorCode errorCode, String value) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .date(LocalDateTime.now())
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(value + "는(은) " + errorCode.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> errorResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .date(LocalDateTime.now())
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build());
    }
}
