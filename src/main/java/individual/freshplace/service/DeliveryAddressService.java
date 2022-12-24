package individual.freshplace.service;

import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressUpdateRequest;
import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DeliveryAddressRepository;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.CustomAuthenticationException;
import individual.freshplace.util.exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final MemberRepository memberRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    @Transactional(readOnly = true)
    public List<DeliveryAddressResponse> getDeliveryAddresses(final String memberId) {
        final Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));
        return member.getAddressList().stream().map(DeliveryAddressResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public DeliveryAddressResponse addDeliveryAddress(final String memberId, final DeliveryAddressAddRequest deliverAddressAddRequest) {
        final Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));
        final DeliverAddress deliverAddress = deliverAddressAddRequest.toDeliverAddress(member);
        deliveryAddressRepository.save(deliverAddress);
        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressZipCode(final String memberId, final DeliveryAddressUpdateRequest.ZipCode zipCode) {
        final DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, zipCode.getDeliverSeq());
        deliverAddress.getAddress().updateZipCode(zipCode.getZipCode());
        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressDetailAddress(final String memberId, final DeliveryAddressUpdateRequest.Address address) {
        final DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, address.getDeliverSeq());
        deliverAddress.getAddress().updateAddress(address.getAddress());
        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressRecipient(final String memberId, final DeliveryAddressUpdateRequest.Recipient recipient) {
        final DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, recipient.getDeliverSeq());
        deliverAddress.updateRecipient(recipient.getRecipient());
        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public DeliveryAddressResponse updateDeliveryAddressContact(final String memberId, final DeliveryAddressUpdateRequest.Contact contact) {
        final DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, contact.getDeliverSeq());
        deliverAddress.updateContact(contact.getContact());
        return DeliveryAddressResponse.from(deliverAddress);
    }

    @Transactional
    public void deleteDeliveryAddress(final String memberId, final Long deliverSeq) {
        final DeliverAddress deliverAddress = getDeliveryAddressAndMemberCheck(memberId, deliverSeq);
        deliveryAddressRepository.delete(deliverAddress);
    }

    private DeliverAddress getDeliveryAddressAndMemberCheck(final String memberId, final Long deliverSeq) {
        final Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NonExistentException(ErrorCode.BAD_VALUE, memberId));
        final DeliverAddress deliveryAddress = deliveryAddressRepository.getById(deliverSeq);

        if (member != deliveryAddress.getMember()) {
            throw new CustomAuthenticationException(ErrorCode.NON_PERMISSION);
        }
        return deliveryAddress;
    }

}
