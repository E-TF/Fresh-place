package individual.freshplace.repository;

import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAddressRepository extends JpaRepository<DeliverAddress, Long> {

    List<DeliverAddress> findAllByMember(Member member);
}
