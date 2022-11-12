package individual.freshplace.util;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.Arrays;

public class CookieUtils {

    private static final Duration COOKIE_CONNECTION_BEGINNING = Duration.ofDays(3);
    private static final Duration COOKIE_CONNECTION_TERMINATE = Duration.ofDays(0);

    public static Cookie executeSetCookie(String itemSeq, String itemCounting, String path) {
        Cookie cookie = new Cookie(itemSeq, itemCounting);
        cookie.setMaxAge((int)COOKIE_CONNECTION_BEGINNING.toHours());
        cookie.setPath(path);
        return cookie;
    }

    public static Cookie executeRemoveCookie(Cookie cookie, String path) {
        cookie.setMaxAge((int)COOKIE_CONNECTION_TERMINATE.toHours());
        cookie.setPath(path);
        return cookie;
    }

    public static Cookie[] executeRemoveCookies(Cookie[] cookies, String path) {
        return Arrays.stream(cookies).peek(cookie -> cookie.setMaxAge(0)).peek(cookie -> cookie.setPath(path)).toArray(Cookie[]::new);
    }
}
