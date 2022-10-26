package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.cart.CartResponse;
import individual.freshplace.service.FCartReadService;
import individual.freshplace.util.CookieUtils;
import individual.freshplace.util.constant.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final FCartReadService fCartReadService;

    @GetMapping("/public/cart")
    public ResponseEntity<List<CartResponse>> getCart(HttpServletRequest request) {

        if (request.getCookies() != null) {
            return ResponseEntity.ok().body(fCartReadService.executeReadCookiesNonMember(request.getCookies()));
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/public/cart")
    public void setCart(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtils.executeSetCookie(request.getParameter(Cart.COOKIE_PARAMETER_SEQ), request.getParameter(Cart.COOKIE_PARAMETER_COUNT), "/");
        response.addCookie(cookie);
        return;
    }

    @DeleteMapping("/public/cart")
    public void removeCart(HttpServletRequest request, HttpServletResponse response) {

        if (!request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = CookieUtils.executeRemoveCookies(request.getCookies(), "/");
            Arrays.stream(cookies).forEach(cookie -> response.addCookie(cookie));
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = request.getCookies();
            String itemSeq = request.getParameter(Cart.COOKIE_PARAMETER_SEQ);
            Arrays.stream(cookies).filter(cookie -> itemSeq.equals(cookie.getName()))
                    .peek(cookie -> CookieUtils.executeRemoveCookie(cookie, "/")).forEach(cookie -> response.addCookie(cookie));
            return;
        }
    }

    @GetMapping("/members/cart")
    public ResponseEntity<List<CartResponse>> getCartMember(HttpServletRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        if (request.getCookies() != null) {
            return ResponseEntity.ok().body(fCartReadService.executeReadCookiesMember(principalDetails.getUsername(), request.getCookies()));
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/cart")
    public void setCartMember(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtils.executeSetCookie(request.getParameter(Cart.COOKIE_PARAMETER_SEQ), request.getParameter(Cart.COOKIE_PARAMETER_COUNT), "/members");
        response.addCookie(cookie);
        return;
    }

    @DeleteMapping("/members/cart")
    public void removeCartMember(HttpServletRequest request, HttpServletResponse response) {

        if (!request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = CookieUtils.executeRemoveCookies(request.getCookies(), "/members");
            Arrays.stream(cookies).forEach(cookie -> response.addCookie(cookie));
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = request.getCookies();
            String itemSeq = request.getParameter(Cart.COOKIE_PARAMETER_SEQ);
            Arrays.stream(cookies).filter(cookie -> itemSeq.equals(cookie.getName()))
                    .peek(cookie -> CookieUtils.executeRemoveCookie(cookie, "/members")).forEach(cookie -> response.addCookie(cookie));
            return;
        }
    }
}
