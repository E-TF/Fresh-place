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
public class KakaoPayPaymentCancelResponse {

    private final String aid;
    private final String tid;
    private final String cid;
    private final String status;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String paymentMethodType;
    private final Amount amount;
    private final ApprovedCancelAmount approvedCancelAmount;
    private final CanceledAmount canceledAmount;
    private final CancelAvailableAmount cancelAvailableAmount;
    private final String itemName;
    private final String itemCode;
    private final long quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;
    private final LocalDateTime canceledAt;
    private final String payload;

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
    public static class ApprovedCancelAmount {
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
    static class CancelAvailableAmount {
        private final long total;
        private final long taxFree;
        private final long vat;
        private final long point;
        private final long discount;
        private final long greenDeposit;
    }
}
