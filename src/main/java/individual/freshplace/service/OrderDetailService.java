package individual.freshplace.service;

import individual.freshplace.entity.OrderDetail;
import individual.freshplace.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Transactional
    public void save(final OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
