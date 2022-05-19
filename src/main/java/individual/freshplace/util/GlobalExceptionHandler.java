package individual.freshplace.util;

import individual.freshplace.util.exception.DuplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity<ErrorResponse> duplicationExceptionHandler(DuplicationException e) {
        return ErrorResponse.errorResponse(e.getErrorCode());
    }
}