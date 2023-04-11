package individual.freshplace.util;

import individual.freshplace.util.auth.jwt.JwtTokenProvider;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;

public class CookieUtils {

    private static final String COOKIE_PATH_FOR_PUBLIC = "/";
    private static final Duration COOKIE_CONNECTION_BEGINNING = Duration.ofDays(3);
    private static final Duration COOKIE_CONNECTION_TERMINATE = Duration.ofDays(0);
    private static final String COOKIE_STANDARD_SAME_SITE = "None";

    public static Cookie createCookie(String itemSeq, String itemCounting) {
        Cookie cookie = new Cookie(itemSeq, itemCounting);
        cookie.setMaxAge((int)COOKIE_CONNECTION_BEGINNING.toHours());
        cookie.setPath(COOKIE_PATH_FOR_PUBLIC);
        return cookie;
    }

    public static ResponseCookie createRefreshToken(String refreshToken) {
        return ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_SUBJECT, refreshToken)
                .maxAge(JwtTokenProvider.REFRESH_TOKEN_EXPIRATION_TIME)
                .httpOnly(true)
                .secure(true)
                .sameSite(COOKIE_STANDARD_SAME_SITE)
                .path(COOKIE_PATH_FOR_PUBLIC)
                .build();
    }

    public static Cookie removeCookie(Cookie cookie) {
        cookie.setMaxAge((int)COOKIE_CONNECTION_TERMINATE.toHours());
        cookie.setPath(COOKIE_PATH_FOR_PUBLIC);
        return cookie;
    }

    public static Cookie[] removeCookies(Cookie[] cookies) {
        return Arrays.stream(cookies).peek(cookie -> cookie.setMaxAge((int)COOKIE_CONNECTION_TERMINATE.toHours()))
                .peek(cookie -> cookie.setPath(COOKIE_PATH_FOR_PUBLIC)).toArray(Cookie[]::new);
    }

    public static ResponseCookie removeRefreshToken(String refreshToken) {
        return ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_SUBJECT, refreshToken)
                .maxAge(COOKIE_CONNECTION_TERMINATE)
                .httpOnly(true)
                .secure(true)
                .sameSite(COOKIE_STANDARD_SAME_SITE)
                .path(COOKIE_PATH_FOR_PUBLIC)
                .build();
    }

    public static boolean exitsRefreshToken(Cookie[] cookies) {
        return Arrays.stream(cookies).anyMatch(cookie -> JwtTokenProvider.REFRESH_TOKEN_SUBJECT.equals(cookie.getName()));
    }

    public static Cookie getRefreshToken(Cookie[] cookies) {
        return Arrays.stream(cookies).filter(cookie -> JwtTokenProvider.REFRESH_TOKEN_SUBJECT.equals(cookie.getName()))
                .findFirst().orElseThrow();
    }
}
