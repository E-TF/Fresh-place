package individual.freshplace.service;

import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.repository.DeliveryAddressRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;

    @Transactional(readOnly = true)
    public DeliverAddress findById(final Long id) {
        return deliveryAddressRepository.findById(id)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_VALUE, id.toString()));
    }

    @Transactional
    public void save(final DeliverAddress deliverAddress) {
        deliveryAddressRepository.save(deliverAddress);
    }
}
