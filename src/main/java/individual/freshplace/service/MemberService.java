package individual.freshplace.service;

import individual.freshplace.entity.Member;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.WrongValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member findByMemberId(final String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new WrongValueException(ErrorCode.USERNAME_NOT_FOUND, memberId));
    }

    @Transactional(readOnly = true)
    public boolean existsByMemberId(final String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public void save(final Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void deleteByMemberId(final String memberId) {
        memberRepository.deleteByMemberId(memberId);
    }
}
