package individual.freshplace.service;

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
        if (isNotNull(authorization) || !CookieUtils.exitsRefreshToken(httpServletRequest.getCookies())) {
            throw new CustomAuthenticationException(ErrorCode.NON_HEADER_AUTHORIZATION);
        }
        try {
            jwtTokenProvider.verifyToken(authorization);
        } catch (TokenExpiredException e) {
            final PrincipalDetails principalDetails = (PrincipalDetails) jwtTokenProvider.getAuthentication(authorization).getPrincipal();
            final String refreshToken = jwtTokenProvider.getRefreshTokenParsing(CookieUtils.getRefreshToken(httpServletRequest.getCookies()).getValue());
            return createReissueAccessToken(principalDetails, refreshToken);
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
            throw new CustomAuthenticationException(ErrorCode.EXPIRED_TOKEN);
        }

        final String refreshToken = jwtTokenProvider.getRefreshTokenParsing(CookieUtils.getRefreshToken(httpServletRequest.getCookies()).getValue());
        final String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (isNotNull(authorization)) {
                return;
            }
            final Authentication authentication = jwtTokenProvider.getAuthentication(authorization);
            final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            checkingAndDeleteRefreshTokenToRepository(principalDetails.getUsername());
        } finally {
            ResponseCookie responseCookie = CookieUtils.removeRefreshToken(refreshToken);
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }
    }

    private boolean isNotNull(String object) {
        return object == null;
    }

    private TokenReissueResponse createReissueAccessToken(final PrincipalDetails principalDetails, final String refreshToken) {
        if (!jwtTokenProvider.validationToken(refreshToken)) {
            checkingAndDeleteRefreshTokenToRepository(principalDetails.getUsername());
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        }
        return new TokenReissueResponse(jwtTokenProvider.createAccessToken(principalDetails));
    }

    private void checkingAndDeleteRefreshTokenToRepository(String userName) {
        String refreshTokenKey = String.format("%s::%s", JwtTokenProvider.REFRESH_TOKEN_SUBJECT, userName);
        if (customRedisTemplate.existsKey(refreshTokenKey)) {
            customRedisTemplate.delete(refreshTokenKey);
        }
    }
}
