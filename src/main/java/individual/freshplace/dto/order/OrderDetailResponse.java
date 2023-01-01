package individual.freshplace.dto.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderDetailResponse {

    private final List<OrderedItem> orderedItems;

    private final long originAmount;
    private final long discountAmount;
    private final long deliveryFee;
    private final long paymentAmount;
    private final String paymentMethod;

    private final String orderer;
    private final LocalDateTime paymentDate;

    private final String receiver;
    private final String receiverPhoneNumber;
    private final String address;
    private final String placeToReceive;
}
