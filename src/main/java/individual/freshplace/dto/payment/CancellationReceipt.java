package individual.freshplace.dto.payment;

import individual.freshplace.dto.order.CanceledItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CancellationReceipt {

    private final List<CanceledItem> canceledItems;
    private final String orderName;
    private final String paymentMethod;
    private final long totalPrice;
    private final LocalDateTime canceledAt;
}
