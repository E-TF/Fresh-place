package individual.freshplace.service;

import individual.freshplace.entity.Order;
import individual.freshplace.entity.Payment;
import individual.freshplace.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void save(final Payment payment) {
        paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment findByOrder(final Order order) {
        return paymentRepository.findByOrder(order);
    }
}
