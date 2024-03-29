package individual.freshplace.dto.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Receipt {

    private final String itemNames;

    private final String recipient;
    private final String deliveryAddress;
    private final String deliveryRequirements;

    private final long totalPrice;
    private final String cardName;
}
