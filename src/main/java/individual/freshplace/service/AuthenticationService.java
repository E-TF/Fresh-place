package individual.freshplace.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import individual.freshplace.dto.auth.login.LoginRequest;
import individual.freshplace.dto.auth.login.LoginResponse;
import individual.freshplace.dto.auth.token.TokenReissueResponse;
import individual.freshplace.util.CookieUtils;
import individual.freshplace.util.CustomRedisTemplate;
import individual.freshplace.util.auth.PrincipalDetails;
import individual.freshplace.util.auth.jwt.JwtTokenProvider;
import individual.freshplace.util.constant.Cache;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomRedisTemplate customRedisTemplate;

    @Transactional
    public LoginResponse login(final LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getMemberId(), loginRequest.getPassword());
        final Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        final String accessToken = jwtTokenProvider.createAccessToken(principalDetails);
        final String refreshToken = jwtTokenProvider.createRefreshToken();
        final ResponseCookie responseCookie = CookieUtils.createRefreshToken(refreshToken);
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        customRedisTemplate.set(String.format("%s::%s", Cache.REFRESH_TOKEN, usernamePasswordAuthenticationToken.getPrincipal().toString()), refreshToken);
        return new LoginResponse(accessToken);
    }

    @Transactional
    public TokenReissueResponse reissue(final HttpServletRequest httpServletRequest) {
        final String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !CookieUtils.exitsRefreshToken(httpServletRequest.getCookies())) {
            throw new CustomAuthenticationException(ErrorCode.NON_HEADER_AUTHORIZATION);
        }

        try {
            jwtTokenProvider.verifyToken(authorization);
        } catch (TokenExpiredException e) {
            Authentication authentication = jwtTokenProvider.getAuthentication(authorization);
            final String refreshToken = jwtTokenProvider.getRefreshTokenParsing(CookieUtils.getRefreshToken(httpServletRequest.getCookies()).getValue());
            final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            if (!jwtTokenProvider.validationToken(refreshToken)) {
                String refreshTokenKey = String.format("%s::%s", JwtTokenProvider.REFRESH_TOKEN_SUBJECT, principalDetails.getUsername());
                if (Boolean.TRUE.equals(customRedisTemplate.existsKey(refreshTokenKey))) {
                    customRedisTemplate.delete(refreshTokenKey);
                }
                throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
            }
            return new TokenReissueResponse(jwtTokenProvider.createAccessToken(principalDetails));
        } catch (SignatureVerificationException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        } catch (JWTVerificationException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        }
        throw new CustomAuthenticationException(ErrorCode.NOT_PASSED_TOKEN_EXPIRED);
    }

    @Transactional
    public void logout(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
        if (!CookieUtils.exitsRefreshToken(httpServletRequest.getCookies())) {
            throw new CustomAuthenticationException(ErrorCode.NON_HEADER_AUTHORIZATION);
        }
        final String refreshToken = jwtTokenProvider.getRefreshTokenParsing(CookieUtils.getRefreshToken(httpServletRequest.getCookies()).getValue());
        final String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            CookieUtils.removeRefreshToken(refreshToken);
            throw new CustomAuthenticationException(ErrorCode.NON_HEADER_AUTHORIZATION);
        }

        Authentication authentication = null;
        try {
            authentication = jwtTokenProvider.getAuthentication(authorization);
        } catch (JWTDecodeException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        } finally {
            ResponseCookie responseCookie = CookieUtils.removeRefreshToken(refreshToken);
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String refreshTokenKey = String.format("%s::%s", JwtTokenProvider.REFRESH_TOKEN_SUBJECT, principalDetails.getUsername());
            if (Boolean.TRUE.equals(customRedisTemplate.existsKey(refreshTokenKey))) {
                customRedisTemplate.delete(refreshTokenKey);
            }
        }
    }
}
