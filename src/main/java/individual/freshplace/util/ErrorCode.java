package individual.freshplace.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 아이디의 회원이 존재하지 않습니다."),
    ID_DUPLICATE_PREVENTION(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    BAD_CODE(HttpStatus.BAD_REQUEST, "존재하지 않는 코드값 입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 에러입니다."),

    FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    RE_REQUEST_LOGIN(HttpStatus.UNAUTHORIZED, "로그인을 다시 시도해주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 유효시간이 완료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 값이 유효하지 않습니다."),
    NON_HEADER_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "헤더값이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}