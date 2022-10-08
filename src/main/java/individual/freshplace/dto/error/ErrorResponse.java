package individual.freshplace.dto.error;

import individual.freshplace.util.constant.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final LocalDateTime date;
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ErrorResponse containsValue(final ErrorCode errorCode, final String value) {
        return new ErrorResponse(
                LocalDateTime.now(), errorCode.getHttpStatus().value(), errorCode.getHttpStatus().name(), errorCode.name(), value + "는(은) " + errorCode.getMessage());
    }

    public static ErrorResponse nothingValue(final ErrorCode errorCode) {
        return new ErrorResponse(
                LocalDateTime.now(), errorCode.getHttpStatus().value(), errorCode.getHttpStatus().name(), errorCode.name(), errorCode.getMessage());
    }

    public static ErrorResponse validation(final ErrorCode errorCode, String validationMessage) {
        return new ErrorResponse(
                LocalDateTime.now(), errorCode.getHttpStatus().value(), errorCode.getHttpStatus().name(), errorCode.name(), validationMessage);
    }
}
