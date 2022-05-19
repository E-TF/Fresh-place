package individual.freshplace.util.exception;

import individual.freshplace.util.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicationException extends RuntimeException {
    private final ErrorCode errorCode;
}