package individual.freshplace.service;

import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.DeliveryAddressRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;

    @Transactional(readOnly = true)
    public DeliverAddress findById(final Long id) {
        return deliveryAddressRepository.findById(id)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_VALUE, id));
    }

    @Transactional(readOnly = true)
    public List<DeliverAddress> findAllByMember(final Member member) {
        return deliveryAddressRepository.findAllByMember(member);
    }

    @Transactional
    public void save(final DeliverAddress deliverAddress) {
        deliveryAddressRepository.save(deliverAddress);
    }
}
