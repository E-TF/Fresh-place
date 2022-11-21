package individual.freshplace.dto.kakaopay;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PayView {

    private final String tid;
    private final String orderId;
    private final String userId;
    private final String qrUrl;

    public static PayView of(String tid, String orderId, String userId, String qrUrl) {
        return new PayView(tid, orderId, userId, qrUrl);
    }
}
