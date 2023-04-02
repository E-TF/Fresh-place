package individual.freshplace.util.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import individual.freshplace.util.auth.PrincipalDetails;
import individual.freshplace.util.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String secret;
    private final String claim;
    public static final String ACCESS_TOKEN_SUBJECT = "accessToken";
    public static final String REFRESH_TOKEN_SUBJECT = "refreshToken";
    public static final Duration ACCESS_TOKEN_EXPIRATION_TIME = Duration.ofMinutes(15);
    public static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(14);

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.claim}") String claim) {
        this.secret = secret;
        this.claim = claim;
    }

    public String createAccessToken(PrincipalDetails principalDetails) {
        return  JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME.toMillis()))
                .withClaim(claim, String.format("%s:%s", principalDetails.getUsername(), principalDetails.getAuthorities().toString()))
                .sign(Algorithm.HMAC512(secret));
    }

    public String createRefreshToken() {
        return  JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME.toMillis()))
                .sign(Algorithm.HMAC512(secret));
    }

    public boolean validationToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.error(ErrorCode.EXPIRED_TOKEN.getMessage());
        } catch (SignatureVerificationException | JWTDecodeException e) {
            log.error(ErrorCode.INVALID_TOKEN.getMessage());
        } catch (NullPointerException e) {
            log.error(ErrorCode.NON_HEADER_AUTHORIZATION.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = new PrincipalDetails(getIdClaim(accessToken), null, getAuthorityClaim(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, Optional.empty(), userDetails.getAuthorities());
    }

    public String getIdClaim(String accessToken) {
        return getClaim(accessToken).asString().split(":")[0];
    }

    public String getAuthorityClaim(String accessToken) {
        return getClaim(accessToken).asString().split(":")[1];
    }

    public String getRefreshToken(String setCookie) {
        return setCookie.replace(REFRESH_TOKEN_SUBJECT+"=", "").split(";")[0];
    }

    private Claim getClaim(String accessToken) {
        return JWT.decode(accessToken).getClaim(claim);
    }
}
