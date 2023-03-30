package individual.freshplace.util.auth.jwt;

import individual.freshplace.util.auth.PrincipalDetailsService;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final PrincipalDetailsService principalDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String memberId = (String) authentication.getPrincipal();
        final String password = (String) authentication.getCredentials();
        final UserDetails userDetails;
        try {
            userDetails = principalDetailsService.loadUserByUsername(memberId);
        } catch (UsernameNotFoundException e) {
            throw new CustomAuthenticationException(ErrorCode.USERNAME_NOT_FOUND);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new CustomAuthenticationException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
