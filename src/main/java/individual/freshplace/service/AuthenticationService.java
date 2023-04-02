package individual.freshplace.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
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

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getMemberId(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.createAccessToken(principalDetails);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        ResponseCookie responseCookie = ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_SUBJECT, refreshToken)
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

        Authentication authentication = null;
        try {
            authentication = jwtTokenProvider.getAuthentication(authorization);
        } catch (JWTDecodeException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        }

        if (!jwtTokenProvider.validationToken(jwtTokenProvider.getRefreshToken(setCookie))) {
            System.out.println(setCookie);
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String refreshTokenKey = JwtTokenProvider.REFRESH_TOKEN_SUBJECT + "::" + principalDetails.getUsername();

        if (Boolean.FALSE.equals(redisTemplate.hasKey(refreshTokenKey))) {
            throw new CustomAuthenticationException(ErrorCode.RE_REQUEST_LOGIN);
        }

        if (!jwtTokenProvider.getRefreshToken(setCookie).equals(redisTemplate.opsForValue().get(refreshTokenKey))) {
            redisTemplate.delete(refreshTokenKey);
            throw new CustomAuthenticationException(ErrorCode.NON_PERMISSION);
        }
        return new TokenReissueResponse(jwtTokenProvider.createAccessToken(principalDetails));
    }
}
