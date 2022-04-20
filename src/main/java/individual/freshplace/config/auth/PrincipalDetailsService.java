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
        System.out.println("PrincipleDetailsService 의 loadUserByUsername() ");
        Member memberEntity = memberRepository.findByMemberId(memberId);

        System.out.println("memberEntity:" + memberEntity.getPassword());
        return new PrincipalDetails(memberEntity);
    }
}
