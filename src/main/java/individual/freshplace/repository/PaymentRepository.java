package individual.freshplace.repository;

import individual.freshplace.entity.Order;
import individual.freshplace.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByOrder(final Order order);
}
