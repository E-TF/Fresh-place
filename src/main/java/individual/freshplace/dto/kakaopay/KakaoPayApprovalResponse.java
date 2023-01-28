package individual.freshplace.dto.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayApprovalResponse {

    private final String aid;
    private final String tid;
    private final String cid;
    private final String sid;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String paymentMethodType;
    private final Amount amount;
    private final Card cardInfo;
    private final String itemName;
    private final String itemCode;
    private final String payload;
    private final long quantity;
    private final long taxFreeAmount;
    private final long vatAmount;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Amount {
        private final long total;
        private final long taxFree;
        private final long vat;
        private final long point;
        private final long discount;
    }

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Card {
        private final String purchaseCorp;
        private final String purchaseCorpCode;
        private final String issuerCorp;
        private final String issuerCorpCode;
        private final String bin;
        private final String cardType;
        private final String installMonth;
        private final String approvedId;
        private final String cardMid;
        private final String interestFreeInstall;
        private final String cardItemCode;
    }
}
