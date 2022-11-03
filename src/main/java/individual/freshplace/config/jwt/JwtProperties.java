package individual.freshplace.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Component
public class JwtProperties {

    public static final Duration EXPIRATION_TIME = Duration.ofHours(24);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = HttpHeaders.AUTHORIZATION;
    public static final String EXCEPTION = "exception";

    private final String secret;
    private final String claim;

    public JwtProperties(@Value("${jwt.secret}") String secret, @Value("${jwt.claim}") String claim) {
        this.secret = secret;
        this.claim = claim;
    }
}
