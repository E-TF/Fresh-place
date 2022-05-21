package individual.freshplace.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private LocalDateTime date;
    private int status;
    private String error;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponse> errorResponse(ErrorCode errorCode, String value) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .date(LocalDateTime.now())
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(value + "는(은) " + errorCode.getMessage())
                        .build()
                );
    }
}
