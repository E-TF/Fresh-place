package individual.freshplace.util.constant.code.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    SUCCESS("P001", "결제성공"),
    FAIL("P002", "결제실패"),
    CANCEL("P003", "결제취소");

    private String codeValue;
    private String codeName;
}
