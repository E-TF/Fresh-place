package individual.freshplace.dto.cart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartOnItem {

    private final String itemName;
    private final long itemCounting;
    private final long price;
    private final long discountPrice;

    public static CartOnItem of(final String itemName, final long itemCounting, final long price, final long discountPrice) {
        return new CartOnItem(itemName, itemCounting, price, discountPrice);
    }
}
