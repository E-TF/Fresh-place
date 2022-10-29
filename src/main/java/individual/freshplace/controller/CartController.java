package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.cart.CartResponse;
import individual.freshplace.service.FCartReadService;
import individual.freshplace.util.CookieUtils;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.OutOfInventoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final FCartReadService fCartReadService;
    private static final String COOKIE_PARAMETER_SEQ = "itemSeq";
    private static final String COOKIE_PARAMETER_COUNT = "itemCounting";
    private static final String COOKIE_PATH_FOR_PUBLIC = "/";
    private static final String COOKIE_PATH_FOR_MEMBER = "/members";
    private static final long COOKIE_MAX_SIZE = 7;

    @GetMapping("/public/cart")
    public ResponseEntity<List<CartResponse>> getCart(HttpServletRequest request) {

        if (request.getCookies() != null) {
            return ResponseEntity.ok().body(fCartReadService.executeReadCookiesNonMember(request.getCookies()));
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/public/cart")
    public void setCart(HttpServletRequest request, HttpServletResponse response) {

        //새 아이템이 추가되면 장바구니 모든 유지시간을 다시 셋팅.
        if (request.getCookies() != null) {
            Cookie[] cookies = request.getCookies();

            if(cookies.length >= COOKIE_MAX_SIZE) {
                throw new OutOfInventoryException(ErrorCode.FAILED_ADD_ITEM_TO_CART, String.valueOf(COOKIE_MAX_SIZE));
            }

            Arrays.stream(cookies).forEach(cookie -> response.addCookie(CookieUtils.executeSetCookie(cookie.getName(), cookie.getValue(), COOKIE_PATH_FOR_PUBLIC)));
        }

        Cookie cookie = CookieUtils.executeSetCookie(request.getParameter(COOKIE_PARAMETER_SEQ), request.getParameter(COOKIE_PARAMETER_COUNT), COOKIE_PATH_FOR_PUBLIC);
        response.addCookie(cookie);
        return;
    }

    @DeleteMapping("/public/cart")
    public void removeCart(HttpServletRequest request, HttpServletResponse response) {

        if (!request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = CookieUtils.executeRemoveCookies(request.getCookies(), COOKIE_PATH_FOR_PUBLIC);
            Arrays.stream(cookies).forEach(cookie -> response.addCookie(cookie));
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = request.getCookies();
            String itemSeq = request.getParameter(COOKIE_PARAMETER_SEQ);
            Arrays.stream(cookies).filter(cookie -> itemSeq.equals(cookie.getName()))
                    .peek(cookie -> CookieUtils.executeRemoveCookie(cookie, COOKIE_PATH_FOR_PUBLIC)).forEach(cookie -> response.addCookie(cookie));
            return;
        }
    }

    @GetMapping("/members/cart")
    public ResponseEntity<List<CartResponse>> getCartMember(HttpServletRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (request.getCookies() != null) {
            return ResponseEntity.ok().body(fCartReadService.executeReadCookiesMember(principalDetails.getUsername(), request.getCookies()));
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/cart")
    public void setCartMember(HttpServletRequest request, HttpServletResponse response) {

        //새 아이템이 추가되면 장바구니 모든 유지시간을 다시 셋팅.
        if (request.getCookies() != null) {
            Cookie[] cookies = request.getCookies();

            if(cookies.length >= COOKIE_MAX_SIZE) {
                throw new OutOfInventoryException(ErrorCode.FAILED_ADD_ITEM_TO_CART, String.valueOf(COOKIE_MAX_SIZE));
            }

            Arrays.stream(cookies).forEach(cookie -> response.addCookie(CookieUtils.executeSetCookie(cookie.getName(), cookie.getValue(), COOKIE_PATH_FOR_MEMBER)));
        }

        Cookie cookie = CookieUtils.executeSetCookie(request.getParameter(COOKIE_PARAMETER_SEQ), request.getParameter(COOKIE_PARAMETER_COUNT), COOKIE_PATH_FOR_MEMBER);
        response.addCookie(cookie);
        return;
    }

    @DeleteMapping("/members/cart")
    public void removeCartMember(HttpServletRequest request, HttpServletResponse response) {

        if (!request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = CookieUtils.executeRemoveCookies(request.getCookies(), COOKIE_PATH_FOR_MEMBER);
            Arrays.stream(cookies).forEach(cookie -> response.addCookie(cookie));
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = request.getCookies();
            String itemSeq = request.getParameter(COOKIE_PARAMETER_SEQ);
            Arrays.stream(cookies).filter(cookie -> itemSeq.equals(cookie.getName()))
                    .peek(cookie -> CookieUtils.executeRemoveCookie(cookie, COOKIE_PATH_FOR_MEMBER)).forEach(cookie -> response.addCookie(cookie));
            return;
        }
    }
}
