package individual.freshplace.dto.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayOrderDetailsResponse {

    private String tid;
    private String cid;
    private String status;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private Amount amount;
    private CanceledAmount canceledAmount;
    private CanceledAvailableAmount canceledAvailableAmount;
    private String itemName;
    private String itemCode;
    private String quantity;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;
    private SelectedCardInfo selectedCardInfo;
    private PaymentActionDetails[] paymentActionDetails;

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
        private long greenDeposit;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class CanceledAmount {
        private long total;
        private long taxFree;
        private long vat;
        private long point;
        private long discount;
        private long greenDeposit;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class CanceledAvailableAmount {
        private long total;
        private long taxFree;
        private long vat;
        private long point;
        private long discount;
        private long greenDeposit;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class SelectedCardInfo {
        private String cardBin;
        private long installMonth;
        private String cardCorpName;
        private String interestFreeInstall;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class PaymentActionDetails {
        private String aid;
        private String approvedAt;
        private long amount;
        private long pointAmount;
        private long discountAmount;
        private long greenDeposit;
        private String paymentActionType;
        private String payload;
    }
}
