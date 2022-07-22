package individual.freshplace.service;

import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressUpdateRequest;
import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DeliveryAddressRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliverAddressRepository;

    @Transactional(readOnly = true)
    public List<DeliveryAddressResponse> getDeliveryAddresses(final Member member) {
        return deliverAddressRepository.findAllByMember(member)
                .stream().map(DeliveryAddressResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public DeliveryAddressResponse AddDeliveryAddress(final Member member, DeliveryAddressAddRequest deliverAddressAddRequest) {

        DeliverAddress deliverAddress = deliverAddressAddRequest.toDeliverAddress(member);

        deliverAddressRepository.save(deliverAddress);

        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressZipCode(DeliveryAddressUpdateRequest.ZipCode zipCode) {

        DeliverAddress deliveryAddress = findDeliveryAddress(zipCode.getDeliverSeq());

        deliveryAddress.getAddress().updateZipCode(zipCode.getZipCode());

        return DeliveryAddressResponse.from(deliveryAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressDetailAddress(DeliveryAddressUpdateRequest.Address address) {

        DeliverAddress deliveryAddress = findDeliveryAddress(address.getDeliverSeq());

        deliveryAddress.getAddress().updateAddress(address.getAddress());

        return DeliveryAddressResponse.from(deliveryAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressRecipient(DeliveryAddressUpdateRequest.Recipient recipient) {

        DeliverAddress deliveryAddress = findDeliveryAddress(recipient.getDeliverSeq());

        deliveryAddress.updateRecipient(recipient.getRecipient());

        return DeliveryAddressResponse.from(deliveryAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressContact(DeliveryAddressUpdateRequest.Contact contact) {
        DeliverAddress deliveryAddress = findDeliveryAddress(contact.getDeliverSeq());

        deliveryAddress.updateContact(contact.getContact());

        return DeliveryAddressResponse.from(deliveryAddress);
    }

    private DeliverAddress findDeliveryAddress(Long deliverSeq) {
        return deliverAddressRepository.findById(deliverSeq)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_VALUE, deliverSeq));
    }
}
