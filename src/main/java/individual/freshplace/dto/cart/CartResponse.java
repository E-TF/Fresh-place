package individual.freshplace.dto.cart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CartResponse {

    private final List<CartOnItem> cartOnItems;

    public static CartResponse from(final List<CartOnItem> cartOnItems) {
        return new CartResponse(cartOnItems);
    }
}
