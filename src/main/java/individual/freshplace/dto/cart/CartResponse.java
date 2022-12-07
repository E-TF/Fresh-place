package individual.freshplace.dto.cart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CartResponse {

    private final List<CartItem> cartItems;
}
