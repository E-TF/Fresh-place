package individual.freshplace.dto.deliveryaddress;

import individual.freshplace.entity.Address;
import individual.freshplace.entity.DeliverAddress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeliveryAddressResponse {

    private final long deliverSeq;

    private final Address address;

    private final String recipient;

    private final String contact;

    public static DeliveryAddressResponse from(final DeliverAddress deliverAddress) {
        return new DeliveryAddressResponse(deliverAddress.getDeliverSeq(), deliverAddress.getAddress(), deliverAddress.getRecipient(), deliverAddress.getContact());
    }
}
