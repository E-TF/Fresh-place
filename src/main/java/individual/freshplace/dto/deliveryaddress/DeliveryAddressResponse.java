package individual.freshplace.dto.deliveryaddress;

import individual.freshplace.entity.Address;
import individual.freshplace.entity.DeliverAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryAddressResponse {

    private Address address;

    private String recipient;

    private String contact;

    public static DeliveryAddressResponse from(final DeliverAddress deliverAddress) {
        return new DeliveryAddressResponse(deliverAddress.getAddress(), deliverAddress.getRecipient(), deliverAddress.getContact());
    }
}
