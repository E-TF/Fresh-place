package individual.freshplace.util.constant.code.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    READY("D001", "배송준비"),
    START("D002", "배송시작"),
    COMPLETE("D003", "배송완료");

    private String codeValue;
    private String codeName;
}
