package individual.freshplace.repository;

import individual.freshplace.entity.DeliverAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliverAddress, Long> {
}
