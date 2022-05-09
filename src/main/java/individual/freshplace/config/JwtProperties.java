package individual.freshplace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    private String secret;

    public JwtProperties(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String getSecretKey() {
        return this.secret;
    }

    public static final int EXPIRATION_TIME = 1000 * 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
