package individual.freshplace.util.exception;

import individual.freshplace.util.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WrongValueException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String value;
}
