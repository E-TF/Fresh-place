package individual.freshplace.repository;

import individual.freshplace.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByMemberId(String memberId);

    void deleteByMemberId(String memberId);
}
