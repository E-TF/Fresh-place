package individual.freshplace.dto.order;

import lombok.Getter;

@Getter
public class OrderRequest {

    private long recipientInformation;
    private String deliveryRequirements;
}
