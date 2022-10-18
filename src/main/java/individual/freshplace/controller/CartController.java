package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.item.ItemResponse;
import individual.freshplace.dto.profile.ProfileResponse;
import individual.freshplace.service.FItemService;
import individual.freshplace.service.FProfileService;
import individual.freshplace.util.constant.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@RestController
@RequiredArgsConstructor
public class CartController {

    static final String COOKIE_PARAMETER_SEQ = "itemSeq";
    static final String COOKIE_PARAMETER_COUNT = "itemCounting";
    static final String COOKIE_EMPTY_STRING = "장바구니가 비어있습니다.";

    private final FItemService fItemService;
    private final FProfileService fProfileService;

    @GetMapping("/public/cart")
    public void getCart(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getParameterNames().hasMoreElements()) {
            Cookie cookie = executeSetCookie(request.getParameter(COOKIE_PARAMETER_SEQ), request.getParameter(COOKIE_PARAMETER_COUNT), "/");
            response.addCookie(cookie);
            return;
        }

        if (request.getCookies() == null) {
            response.getWriter().write(URLDecoder.decode(COOKIE_EMPTY_STRING, "UTF-8"));
        }

        else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {

                Long itemSeq = Long.parseLong(cookie.getName());
                Long itemCounting = Long.parseLong(cookie.getValue());
                ItemResponse itemResponse = fItemService.getItemDetail(itemSeq);
                response.getWriter().write((itemResponse.getItemName() + " " + itemCounting + " " + itemResponse.getPrice() * itemCounting + " " + itemResponse.getPrice() * itemCounting));
            }
        }
    }

    @GetMapping("/members/cart")
    public void getCart(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        if (request.getParameterNames().hasMoreElements()) {
            Cookie cookie = executeSetCookie(request.getParameter(COOKIE_PARAMETER_SEQ), request.getParameter(COOKIE_PARAMETER_COUNT), "/members");
            response.addCookie(cookie);
            return;
        }

        if (request.getCookies() == null) {
            response.getWriter().write(URLDecoder.decode(COOKIE_EMPTY_STRING, "UTF-8"));
        }

        else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {

                Long itemSeq = Long.parseLong(cookie.getName());
                Long itemCounting = Long.parseLong(cookie.getValue());

                ItemResponse itemResponse = fItemService.getItemDetail(itemSeq);
                ProfileResponse profileResponse = fProfileService.getProfile(principalDetails.getUsername());

                long discountRate = Membership.findByCodeName(profileResponse.getGradeName()).getDiscountRate();
                long discountPrice = Math.round(itemResponse.getPrice() * itemCounting * (100-discountRate) / 100.0);
                response.getWriter().write((itemResponse.getItemName() + " " + itemCounting + " " + itemResponse.getPrice() * itemCounting + " " + discountPrice));
            }
        }

    }

    @DeleteMapping("/public/cart")
    public void removeCart(HttpServletRequest request, HttpServletResponse response) {

        if (!request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = executeRemoveCookies(request.getCookies(), "/");
            for (Cookie cookie : cookies) {
                response.addCookie(cookie);
            }
            return;
        }

        String itemSeq = request.getParameter(COOKIE_PARAMETER_SEQ);
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (itemSeq.equals(cookie.getName())) {
                Cookie ageSetZeroCookie = executeRemoveCookie(cookie, "/");
                response.addCookie(ageSetZeroCookie);
                return;
            }
        }

    }

    @DeleteMapping("/members/cart")
    public void removeCart(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (!request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = executeRemoveCookies(request.getCookies(), "/members");
            for (Cookie cookie : cookies) {
                response.addCookie(cookie);
            }
            return;
        }

        String itemSeq = request.getParameter(COOKIE_PARAMETER_SEQ);
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (itemSeq.equals(cookie.getName())) {
                Cookie ageSetZeroCookie = executeRemoveCookie(cookie, "/members");
                response.addCookie(ageSetZeroCookie);
                return;
            }
        }

    }

    private Cookie executeSetCookie(String itemSeq, String itemCounting, String path) {
        Cookie cookie = new Cookie(itemSeq, itemCounting);
        cookie.setMaxAge(3600 * 12);
        cookie.setPath(path);
        return cookie;
    }

    private Cookie executeRemoveCookie(Cookie cookie, String path) {
        cookie.setMaxAge(0);
        cookie.setPath(path);
        return cookie;
    }

    private Cookie[] executeRemoveCookies(Cookie[] cookies, String path) {
        Cookie[] returnCookies = new Cookie[cookies.length];
        for (int i=0; i<returnCookies.length; i++) {
            cookies[i].setMaxAge(0);
            cookies[i].setPath(path);
            returnCookies[i] = cookies[i];
        }
        return returnCookies;
    }
}
