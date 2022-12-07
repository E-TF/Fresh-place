package individual.freshplace.util.constant.code.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    SUCCESS("O001", "주문성공"),
    FAIL("O002", "주문실패"),
    CANCEL("O003", "주문취소");

    private String codeValue;
    private String codeName;
}
