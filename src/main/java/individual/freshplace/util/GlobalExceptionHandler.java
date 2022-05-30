package individual.freshplace.util;

import individual.freshplace.util.exception.DuplicationException;
import individual.freshplace.util.exception.WrongValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity<ErrorResponse> duplicationExceptionHandler(final DuplicationException e) {
        return ErrorResponse.errorResponse(e.getErrorCode(), e.getValue());
    }

    @ExceptionHandler(value = WrongValueException.class)
    public ResponseEntity<ErrorResponse> wrongValueExceptionHandler(final WrongValueException e) {
        return ErrorResponse.errorResponse(e.getErrorCode(), e.getValue());
    }
}