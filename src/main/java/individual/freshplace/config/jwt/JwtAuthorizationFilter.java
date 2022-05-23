package individual.freshplace.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import individual.freshplace.config.JwtProperties;
import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.MemberRepository;
import individual.freshplace.util.ErrorCode;
import individual.freshplace.util.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

        try {
            String memberId = decryptToken(request, changeToken);
            Authentication authentication = getAuthentication(memberId);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            log.error("권한이 거부되었습니다");
        } finally {
            chain.doFilter(request, response);
        }

    }

    private String getTokenInfo(HttpServletRequest httpServletRequest) {

        String tokenValue = httpServletRequest.getHeader(JwtProperties.HEADER_STRING);
        if (StringUtils.hasLength(tokenValue) && tokenValue.startsWith(JwtProperties.TOKEN_PREFIX)) {
            tokenValue = tokenValue.replace(JwtProperties.TOKEN_PREFIX, "");
        }

        return tokenValue;
    }

    private String decryptToken(HttpServletRequest request, String changeToken) {

        try {
            return JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build().verify(changeToken).getClaim(jwtProperties.getClaim()).asString();
        } catch (TokenExpiredException e) {
            request.setAttribute(JwtProperties.EXCEPTION, ErrorCode.EXPIRED_TOKEN.name());
            throw new AuthenticationException();
        } catch (SignatureVerificationException e) {
            request.setAttribute(JwtProperties.EXCEPTION, ErrorCode.INVALID_TOKEN.name());
            throw new AuthenticationException();
        } catch (NullPointerException e) {
            request.setAttribute(JwtProperties.EXCEPTION, ErrorCode.NON_HEADER_AUTHORIZATION.name());
            throw new AuthenticationException();
        }
    }

    private Authentication getAuthentication(String memberId) {

        Member member = memberRepository.findByMemberId(memberId).orElseThrow();

        PrincipalDetails principalDetails = new PrincipalDetails(member.getMemberId(), member.getPassword());
        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }
}