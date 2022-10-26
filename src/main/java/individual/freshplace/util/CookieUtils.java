package individual.freshplace.util;

import javax.servlet.http.Cookie;
import java.util.Arrays;

public class CookieUtils {

    public static Cookie executeSetCookie(String itemSeq, String itemCounting, String path) {
        Cookie cookie = new Cookie(itemSeq, itemCounting);
        cookie.setMaxAge(3600 * 12);
        cookie.setPath(path);
        return cookie;
    }

    public static Cookie executeRemoveCookie(Cookie cookie, String path) {
        cookie.setMaxAge(0);
        cookie.setPath(path);
        return cookie;
    }

    public static Cookie[] executeRemoveCookies(Cookie[] cookies, String path) {
        return Arrays.stream(cookies).peek(cookie -> cookie.setMaxAge(0)).peek(cookie -> cookie.setPath(path)).toArray(Cookie[]::new);
    }
}
