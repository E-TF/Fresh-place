package individual.freshplace.service;

import individual.freshplace.entity.Order;
import individual.freshplace.repository.OrderRepository;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void save(final Order order) {
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getById(final Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new WrongValueException(ErrorCode.BAD_VALUE, id.toString()));
    }
}
