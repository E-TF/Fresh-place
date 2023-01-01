package individual.freshplace.dto.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderedItem {

    private final String itemName;
    private final long itemCounting;
    private final long totalPrice;
}
