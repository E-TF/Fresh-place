package individual.freshplace.service;

import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DeliveryAddressRepository;
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
}
