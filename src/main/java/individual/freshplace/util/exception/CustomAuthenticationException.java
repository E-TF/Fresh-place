package individual.freshplace.util.exception;

import individual.freshplace.util.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomAuthenticationException extends RuntimeException {
    private final ErrorCode errorCode;
}
