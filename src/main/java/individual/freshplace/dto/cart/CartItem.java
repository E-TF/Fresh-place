package individual.freshplace.dto.cart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartItem {

    private final long itemSeq;
    private final String itemName;
    private final long itemCounting;
    private final long price;
    private final long discountPrice;

    public static CartItem of(final long itemSeq, final String itemName, final long itemCounting, final long price, final long discountPrice) {
        return new CartItem(itemSeq, itemName, itemCounting, price, discountPrice);
    }
}
