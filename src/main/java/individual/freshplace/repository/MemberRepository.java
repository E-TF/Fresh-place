package individual.freshplace.repository;

import individual.freshplace.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findByMemberId(String memberId);
}
