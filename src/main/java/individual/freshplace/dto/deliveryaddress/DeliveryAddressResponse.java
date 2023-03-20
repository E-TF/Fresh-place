package individual.freshplace.dto.deliveryaddress;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import individual.freshplace.entity.Address;
import individual.freshplace.entity.DeliverAddress;
import lombok.Getter;

@Getter
public class DeliveryAddressResponse {

    private final long deliverSeq;

    private final Address address;

    private final String recipient;

    private final String contact;

    @JsonCreator
    private DeliveryAddressResponse(@JsonProperty("deliverSeq") long deliverSeq, @JsonProperty("address") Address address,
                                    @JsonProperty("recipient") String recipient, @JsonProperty("contact") String contact) {

        this.deliverSeq = deliverSeq;
        this.address = address;
        this.recipient = recipient;
        this.contact = contact;
    }

    public static DeliveryAddressResponse from(final DeliverAddress deliverAddress) {
        return new DeliveryAddressResponse(deliverAddress.getDeliverSeq(), deliverAddress.getAddress(), deliverAddress.getRecipient(), deliverAddress.getContact());
    }
}
