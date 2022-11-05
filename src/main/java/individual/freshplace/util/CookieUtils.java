package individual.freshplace.util;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;

public class CookieUtils {

    private static final String COOKIE_PATH_FOR_PUBLIC = "/";
    private static final Duration COOKIE_CONNECTION_BEGINNING = Duration.ofDays(3);
    private static final Duration COOKIE_CONNECTION_TERMINATE = Duration.ofDays(0);

    public static Cookie setCookie(String itemSeq, String itemCounting) {
        Cookie cookie = new Cookie(itemSeq, itemCounting);
        cookie.setMaxAge((int)COOKIE_CONNECTION_BEGINNING.toHours());
        cookie.setPath(COOKIE_PATH_FOR_PUBLIC);
        return cookie;
    }

    public static Cookie removeCookie(Cookie cookie) {
        cookie.setMaxAge((int)COOKIE_CONNECTION_TERMINATE.toHours());
        cookie.setPath(COOKIE_PATH_FOR_PUBLIC);
        return cookie;
    }

    public static Cookie[] removeCookies(Cookie[] cookies) {
        return Arrays.stream(cookies).peek(cookie -> cookie.setMaxAge((int)COOKIE_CONNECTION_TERMINATE.toHours())).peek(cookie -> cookie.setPath(COOKIE_PATH_FOR_PUBLIC)).toArray(Cookie[]::new);
    }
}
