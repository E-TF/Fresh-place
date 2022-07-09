package individual.freshplace.repository;

import individual.freshplace.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    List<Member> findAllByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    void deleteByMemberId(String memberId);
}
