package individual.freshplace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtProperties {

    private String secret;

    public JwtProperties(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String getSecretKey() {
        return this.secret;
    }

    public static final long EXPIRATION_TIME_TO_MillSECOND = Duration.ofHours(1).toMillis();
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = HttpHeaders.AUTHORIZATION;
}
