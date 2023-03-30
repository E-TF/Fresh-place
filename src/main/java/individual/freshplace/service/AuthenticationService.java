package individual.freshplace.service;

import individual.freshplace.dto.auth.login.LoginRequest;
import individual.freshplace.dto.auth.login.LoginResponse;
import individual.freshplace.dto.auth.token.TokenReissueRequest;
import individual.freshplace.dto.auth.token.TokenReissueResponse;
import individual.freshplace.util.auth.PrincipalDetails;
import individual.freshplace.util.auth.jwt.JwtTokenProvider;
import individual.freshplace.util.constant.Cache;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public LoginResponse login(final LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getMemberId(), loginRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.createAccessToken(principalDetails);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisTemplate.opsForValue().set(String.format("%s::%s", Cache.REFRESH_TOKEN, usernamePasswordAuthenticationToken.getPrincipal().toString()), refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenReissueResponse reissue(final TokenReissueRequest reissueTokenRequest) {

        if (!jwtTokenProvider.validationToken(reissueTokenRequest.getRefreshToken())) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
        }

        String refreshTokenKey = JwtTokenProvider.REFRESH_TOKEN_SUBJECT + "::" + jwtTokenProvider.getIdClaim(reissueTokenRequest.getAccessToken());
        if (Boolean.FALSE.equals(redisTemplate.hasKey(refreshTokenKey))) {
            throw new CustomAuthenticationException(ErrorCode.RE_REQUEST_LOGIN);
        }

        if (!reissueTokenRequest.getRefreshToken()
                .equals(redisTemplate.opsForValue().get(refreshTokenKey))) {
            redisTemplate.delete(refreshTokenKey);
            throw new CustomAuthenticationException(ErrorCode.NON_PERMISSION);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissueTokenRequest.getAccessToken());
        String accessToken = jwtTokenProvider.createAccessToken((PrincipalDetails) authentication.getPrincipal());
        return new TokenReissueResponse(accessToken);
    }
}
