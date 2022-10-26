package individual.freshplace.dto.cart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class CartResponse {

    private final String itemName;

    private final long itemCounting;

    private final long price;

    private final long discountPrice;

    public static CartResponse of(final String itemName, final long itemCounting, final long price, final long discountPrice) {
        return new CartResponse(itemName, itemCounting, price, discountPrice);
    }
}
