package individual.freshplace.dto.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayApprovalResponse {

    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private Amount amount;
    private Card cardInfo;
    private String itemName;
    private String itemCode;
    private String payload;
    private long quantity;
    private long taxFreeAmount;
    private long vatAmount;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Amount {
        private long total;
        private long taxFree;
        private long vat;
        private long point;
        private long discount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Card {
        private String purchaseCorp;
        private String purchaseCorpCode;
        private String issuerCorp;
        private String issuerCorpCode;
        private String bin;
        private String cardType;
        private String installMonth;
        private String approvedId;
        private String cardMid;
        private String interestFreeInstall;
        private String cardItemCode;
    }
}
