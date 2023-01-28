package individual.freshplace.util.exception;

import individual.freshplace.util.constant.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileConvertFiledException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String value;
}
