package individual.freshplace.util.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 아이디의 회원이 존재하지 않습니다."),
    ID_DUPLICATE_PREVENTION(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    BAD_CODE(HttpStatus.BAD_REQUEST, "존재하지 않는 코드값 입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 에러입니다."),
    BAD_VALUE(HttpStatus.BAD_REQUEST, "존재하지 않는 값입니다."),
    FILE_ERROR(HttpStatus.BAD_REQUEST, "파일을 확인해주세요,"),
    EMPTY_CART(HttpStatus.BAD_REQUEST, "장바구니가 비어있습니다."),

    FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),
    RE_REQUEST_LOGIN(HttpStatus.UNAUTHORIZED, "로그인을 다시 시도해주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 유효시간이 완료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 값이 유효하지 않습니다."),
    NON_HEADER_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "헤더값이 유효하지 않습니다."),
    NON_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 경로를 찾을 수 없습니다."),

    UN_AVAILABLE_ID(HttpStatus.CONFLICT, "해당 아이디는 사용할 수 없습니다."),

    FAILED_QUERY(HttpStatus.INTERNAL_SERVER_ERROR, "쿼리수행에 실패했습니다."),
    FAILED_FILE_TO_STREAM(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기를 실패했습니다."),
    FAILED_PIXEL_GRABBER(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 비율 축소에 실패했습니다."),
    FAILED_ADD_ITEM_TO_CART(HttpStatus.INTERNAL_SERVER_ERROR, "장바구니 수량은 최대 7개 입니다."),
    NO_STOCK(HttpStatus.INTERNAL_SERVER_ERROR, "재고가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
