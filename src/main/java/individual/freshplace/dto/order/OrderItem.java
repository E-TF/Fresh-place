package individual.freshplace.dto.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderItem {

    private final String itemName;
    private final long itemCount;
    private final long totalPrice;

    public static OrderItem of(String itemName, long itemCount, long totalPrice) {
        return new OrderItem(itemName, itemCount, totalPrice);
    }
}
