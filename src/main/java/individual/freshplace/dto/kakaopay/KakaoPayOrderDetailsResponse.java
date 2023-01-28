package individual.freshplace.dto.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayOrderDetailsResponse {

    private final String tid;
    private final String cid;
    private final String status;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String paymentMethodType;
    private final Amount amount;
    private final CanceledAmount canceledAmount;
    private final CanceledAvailableAmount canceledAvailableAmount;
    private final String itemName;
    private final String itemCode;
    private final String quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;
    private final LocalDateTime canceledAt;
    private final SelectedCardInfo selectedCardInfo;
    private final PaymentActionDetails[] paymentActionDetails;

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
        private final long greenDeposit;
    }

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class CanceledAmount {
        private final long total;
        private final long taxFree;
        private final long vat;
        private final long point;
        private final long discount;
        private final long greenDeposit;
    }

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class CanceledAvailableAmount {
        private final long total;
        private final long taxFree;
        private final long vat;
        private final long point;
        private final long discount;
        private final long greenDeposit;
    }

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class SelectedCardInfo {
        private final String cardBin;
        private final long installMonth;
        private final String cardCorpName;
        private final String interestFreeInstall;
    }

    @Getter
    @NoArgsConstructor(force = true)
    @RequiredArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class PaymentActionDetails {
        private final String aid;
        private final String approvedAt;
        private final long amount;
        private final long pointAmount;
        private final long discountAmount;
        private final long greenDeposit;
        private final String paymentActionType;
        private final String payload;
    }
}
