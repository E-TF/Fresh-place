package individual.freshplace.util.auth;

import individual.freshplace.entity.Member;
import individual.freshplace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        final Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new UsernameNotFoundException(memberId + " 는 찾을 수 없습니다"));
        return new PrincipalDetails(member.getMemberId(), member.getPassword(), member.getRole().name());
    }
}
