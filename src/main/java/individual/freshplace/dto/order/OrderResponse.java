package individual.freshplace.dto.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderResponse {

    private final LocalDateTime createAt;
    private final String statusCode;
    private final String orderName;
    private final String paymentMethod;
    private final long paymentAmount;
}
