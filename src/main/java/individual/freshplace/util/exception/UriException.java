package individual.freshplace.util.exception;

import individual.freshplace.util.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UriException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String value;
}
