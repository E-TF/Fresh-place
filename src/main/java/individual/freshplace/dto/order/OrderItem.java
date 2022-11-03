package individual.freshplace.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItem {

    private long itemSeq;
    private String itemName;
    private long itemCount;
    private long totalPrice;
}
