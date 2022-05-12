package individual.freshplace.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import individual.freshplace.config.JwtProperties;
import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.entity.Member;
import individual.freshplace.repository.MemberRepository;
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
import java.util.NoSuchElementException;

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
        String memberId = validateToken(request, changeToken);

        try {
            Authentication authentication = getAuthentication(memberId);

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (NoSuchElementException e) {
            log.info("토큰 검증에 실패하였습니다.");
        }finally {
            chain.doFilter(request, response);
        }
    }

    private String getTokenInfo(HttpServletRequest httpServletRequest) {

        String tokenValue = httpServletRequest.getHeader(JwtProperties.HEADER_STRING);
        if (StringUtils.hasLength(tokenValue) && tokenValue.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return tokenValue.replace(JwtProperties.TOKEN_PREFIX, "");
        }

        return null;
    }

    private Authentication getAuthentication(String memberId) throws NoSuchElementException{

        Member member = memberRepository.findByMemberId(memberId).get();

        PrincipalDetails principalDetails = new PrincipalDetails(member.getMemberId(), member.getPassword());
        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    private String validateToken(HttpServletRequest request, String changeToken) {

        String memberId = null;

        try {
            memberId = JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey()))
                    .build().verify(changeToken).getClaim("id").asString();
        }catch (SignatureVerificationException | TokenExpiredException | NullPointerException e) {
            request.setAttribute(JwtProperties.EXCEPTION, e.getClass().getSimpleName());
        }

        return memberId;
    }
}