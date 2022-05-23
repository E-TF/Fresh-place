package individual.freshplace.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
public class JwtProperties {

    private final String secret;

    private final String claim;

    public JwtProperties(@Value("${jwt.secret}") String secret, @Value("${jwt.claim}") String claim) {
        this.secret = secret;
        this.claim = claim;
    }

    public static final Duration EXPIRATION_TIME = Duration.ofHours(1);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = HttpHeaders.AUTHORIZATION;
    public static final String EXCEPTION = "exception";
}
