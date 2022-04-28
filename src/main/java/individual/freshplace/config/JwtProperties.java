package individual.freshplace.config;

public interface JwtProperties {

    String SECRET_KEY = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmlu" +
            "ZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";
    int EXPIRATION_TIME = 1000 * 60 * 60;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
