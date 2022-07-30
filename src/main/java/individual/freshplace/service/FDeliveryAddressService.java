package individual.freshplace.service;

import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressUpdateRequest;
import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FDeliveryAddressService {

    private final MemberService memberService;
    private final DeliveryAddressService deliveryAddressService;

    @Transactional
    public List<DeliveryAddressResponse> getDeliveryAddresses(final String memberId) {

        Member member = memberService.findByMemberId(memberId);

        return member.getAddressList().stream().map(DeliveryAddressResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public DeliveryAddressResponse addDeliveryAddress(final String memberId, final DeliveryAddressAddRequest deliverAddressAddRequest) {

        Member member = memberService.findByMemberId(memberId);

        DeliverAddress deliverAddress = deliverAddressAddRequest.toDeliverAddress(member);

        deliveryAddressService.save(deliverAddress);

        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressZipCode(final String memberId, final DeliveryAddressUpdateRequest.ZipCode zipCode) {

        DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, zipCode.getDeliverSeq());

        deliverAddress.getAddress().updateZipCode(zipCode.getZipCode());

        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressDetailAddress(final String memberId, final DeliveryAddressUpdateRequest.Address address) {

        DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, address.getDeliverSeq());

        deliverAddress.getAddress().updateAddress(address.getAddress());

        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressRecipient(final String memberId, final DeliveryAddressUpdateRequest.Recipient recipient) {

        DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, recipient.getDeliverSeq());

        deliverAddress.updateRecipient(recipient.getRecipient());

        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressContact(final String memberId, final DeliveryAddressUpdateRequest.Contact contact) {

        DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, contact.getDeliverSeq());

        deliverAddress.updateContact(contact.getContact());

        return DeliveryAddressResponse.from(deliverAddress);
    }

    private DeliverAddress getDeliveryAddressAndMemberCheck(final String memberId, final Long deliverId) {

        Member member = memberService.findByMemberId(memberId);

        DeliverAddress deliveryAddress = deliveryAddressService.findById(deliverId);

        if (member != deliveryAddress.getMember()) {
            throw new CustomAuthenticationException(ErrorCode.NON_PERMISSION);
        }

        return deliveryAddress;
    }
}
