package individual.freshplace.dto.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderItem {

    private final long itemSeq;
    private final String itemName;
    private final long itemCount;
    private final long totalPrice;
}
