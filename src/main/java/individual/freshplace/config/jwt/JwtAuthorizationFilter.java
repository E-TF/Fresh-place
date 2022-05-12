package individual.freshplace.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import individual.freshplace.config.JwtProperties;
import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtProperties jwtProperties) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String changeToken = getTokenInfo(request);

        if (changeToken == null) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getAuthentication(changeToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private String getTokenInfo(HttpServletRequest httpServletRequest) {

        String tokenValue = httpServletRequest.getHeader(JwtProperties.HEADER_STRING);
        if (StringUtils.hasLength(tokenValue) && tokenValue.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return tokenValue.replace(JwtProperties.TOKEN_PREFIX, "");
        }

        return null;
    }

    private Authentication getAuthentication(String changeToken) {

        String memberId = JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey()))
                .build().verify(changeToken).getClaim("id").asString();

        Member member = memberRepository.findByMemberId(memberId).get();

        PrincipalDetails principalDetails = new PrincipalDetails(member.getMemberId(), member.getPassword());
        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }
}