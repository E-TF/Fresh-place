package individual.freshplace.config.auth;

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
        Member memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() -> new UsernameNotFoundException(""));
        return new PrincipalDetails(memberEntity.getMemberId(), memberEntity.getPassword());
    }
}