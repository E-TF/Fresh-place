package individual.freshplace.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import individual.freshplace.dto.auth.login.LoginRequest;
import individual.freshplace.dto.auth.login.LoginResponse;
import individual.freshplace.dto.auth.token.TokenReissueResponse;
import individual.freshplace.util.auth.PrincipalDetails;
import individual.freshplace.util.auth.jwt.JwtTokenProvider;
import individual.freshplace.util.constant.Cache;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public LoginResponse login(final LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getMemberId(), loginRequest.getPassword());

        final Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        final String accessToken = jwtTokenProvider.createAccessToken(principalDetails);
        final String refreshToken = jwtTokenProvider.createRefreshToken();

        final ResponseCookie responseCookie = ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_SUBJECT, refreshToken)
                .maxAge(JwtTokenProvider.REFRESH_TOKEN_EXPIRATION_TIME)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .build();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        redisTemplate.opsForValue().set(String.format("%s::%s", Cache.REFRESH_TOKEN, usernamePasswordAuthenticationToken.getPrincipal().toString()), refreshToken);
        return new LoginResponse(accessToken);
    }

    @Transactional
    public TokenReissueResponse reissue(final HttpServletRequest httpServletRequest) {
        final String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String setCookie = httpServletRequest.getHeader(HttpHeaders.SET_COOKIE);
        if (authorization == null || setCookie == null) {
            throw new CustomAuthenticationException(ErrorCode.NON_HEADER_AUTHORIZATION);
        }
        try {
            jwtTokenProvider.verifyToken(authorization);
        } catch (TokenExpiredException e) {
            Authentication authentication = jwtTokenProvider.getAuthentication(authorization);
            final String refreshToken = jwtTokenProvider.getRefreshToken(setCookie);
            final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            if (!jwtTokenProvider.validationToken(refreshToken)) {
                String refreshTokenKey = String.format("%s::%s", JwtTokenProvider.REFRESH_TOKEN_SUBJECT, principalDetails.getUsername());
                redisTemplate.delete(refreshTokenKey);
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
        final String setCookie = httpServletRequest.getHeader(HttpHeaders.SET_COOKIE);
        if (setCookie == null) {
            throw new CustomAuthenticationException(ErrorCode.NON_HEADER_AUTHORIZATION);
        }
        final String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken = jwtTokenProvider.getRefreshToken(setCookie);
        if (authorization == null) {
            final ResponseCookie responseCookie = ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_SUBJECT, refreshToken)
                    .maxAge(0)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .build();
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            throw new CustomAuthenticationException(ErrorCode.NON_HEADER_AUTHORIZATION);
        }

        try {
            jwtTokenProvider.getAuthentication(authorization);
        } catch (TokenExpiredException e) {
            Authentication authentication = jwtTokenProvider.getAuthentication(authorization);
            final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String refreshTokenKey = String.format("%s::%s", JwtTokenProvider.REFRESH_TOKEN_SUBJECT, principalDetails.getUsername());
            redisTemplate.delete(refreshTokenKey);
        } catch (SignatureVerificationException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        } catch (JWTVerificationException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        } finally {
            final ResponseCookie responseCookie = ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_SUBJECT, refreshToken)
                    .maxAge(0)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .build();
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }
    }
}
