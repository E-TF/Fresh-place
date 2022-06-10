package individual.freshplace.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import individual.freshplace.config.JwtProperties;
import individual.freshplace.config.auth.PrincipalDetailsService;
import individual.freshplace.util.ErrorCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private final PrincipalDetailsService principalDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, PrincipalDetailsService principalDetailsService) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
        this.principalDetailsService = principalDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String changeToken = getTokenInfo(request);

        if (validToken(request, changeToken)) {
            String memberId = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build().verify(changeToken)
                    .getClaim(jwtProperties.getClaim()).asString();

            try {
                UserDetails userDetails = principalDetailsService.loadUserByUsername(memberId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, Optional.empty(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e) {
                request.setAttribute(JwtProperties.EXCEPTION, ErrorCode.RE_REQUEST_LOGIN.name());
            }
        }

        chain.doFilter(request, response);
    }

    private String getTokenInfo(HttpServletRequest httpServletRequest) {

        String tokenValue = httpServletRequest.getHeader(JwtProperties.HEADER_STRING);
        return StringUtils.hasLength(tokenValue) && tokenValue.startsWith(JwtProperties.TOKEN_PREFIX) ? tokenValue.replace(JwtProperties.TOKEN_PREFIX, "") : tokenValue;
    }

    private boolean validToken(HttpServletRequest request, String changeToken) {

        try {
            JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build().verify(changeToken);
            return true;
        } catch (TokenExpiredException e) {
            request.setAttribute(JwtProperties.EXCEPTION, ErrorCode.EXPIRED_TOKEN.name());
        } catch (SignatureVerificationException e) {
            request.setAttribute(JwtProperties.EXCEPTION, ErrorCode.INVALID_TOKEN.name());
        } catch (NullPointerException e) {
            request.setAttribute(JwtProperties.EXCEPTION, ErrorCode.NON_HEADER_AUTHORIZATION.name());
        }

        return false;
    }
}
