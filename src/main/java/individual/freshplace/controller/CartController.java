package individual.freshplace.controller;

import individual.freshplace.dto.cart.CartResponse;
import individual.freshplace.service.CartReadService;
import individual.freshplace.util.CookieUtils;
import individual.freshplace.util.PrincipalUtils;
import individual.freshplace.util.constant.ErrorCode;
import individual.freshplace.util.exception.OutOfInventoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import individual.freshplace.util.constant.Cookies;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/cart-item")
public class CartController {

    private final CartReadService cartReadService;

    @GetMapping
    public ResponseEntity<CartResponse> getItemsInCart(HttpServletRequest request) {

        if (request.getCookies() != null && request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            return ResponseEntity.ok().body(cartReadService.getCartByNonMember(request.getCookies()));
        }

        if (request.getCookies() != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            return ResponseEntity.ok().body(cartReadService.getCartByMember(PrincipalUtils.getUsername(), request.getCookies()));
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public void addItemToCart(HttpServletRequest request, HttpServletResponse response) {

        //새 아이템이 추가되면 장바구니 모든 유지시간을 다시 셋팅.
        if (request.getCookies() != null) {
            Cookie[] cookies = request.getCookies();

            if(cookies.length >= Cookies.COOKIE_MAX_SIZE) {
                throw new OutOfInventoryException(ErrorCode.FAILED_ADD_ITEM_TO_CART, String.valueOf(Cookies.COOKIE_MAX_SIZE));
            }

            Arrays.stream(cookies).forEach(cookie -> response.addCookie(CookieUtils.createCookie(cookie.getName(), cookie.getValue())));
        }

        Cookie cookie = CookieUtils.createCookie(request.getParameter(Cookies.COOKIE_PARAMETER_SEQ), request.getParameter(Cookies.COOKIE_PARAMETER_COUNT));
        response.addCookie(cookie);
    }

    @DeleteMapping
    public void deleteItemInCart(HttpServletRequest request, HttpServletResponse response) {

        if (!request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = CookieUtils.removeCookies(request.getCookies());
            Arrays.stream(cookies).forEach(response::addCookie);
            return;
        }

        if (request.getParameterNames().hasMoreElements()) {
            Cookie[] cookies = request.getCookies();
            String itemSeq = request.getParameter(Cookies.COOKIE_PARAMETER_SEQ);
            Arrays.stream(cookies).filter(cookie -> itemSeq.equals(cookie.getName()))
                    .peek(CookieUtils::removeCookie).forEach(response::addCookie);
        }
    }
}
