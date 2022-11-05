package individual.freshplace.dto.cart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CartResponse {

    private final List<CartOnItems> cartOnItems;

    public static CartResponse from(final List<CartOnItems> cartOnItems) {
        return new CartResponse(cartOnItems);
    }
}
