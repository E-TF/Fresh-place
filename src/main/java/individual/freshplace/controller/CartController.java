package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.cart.CartResponse;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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

        if (!request.getParameterNames().hasMoreElements()) {
            executeCartRead(request, response);
        }

        else {
            Cart cart = getParameterSetCartObject(request);
            CartResponse cartResponse = CartResponse.of(cart.itemName, cart.itemCounting, cart.totalPrice, cart.totalPrice);

            Cookie cookie = executeSetCookie(cart, cartResponse);
            response.addCookie(cookie);

        }
    }

    @GetMapping("/members/cart")
    public void getCart(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        if (!request.getParameterNames().hasMoreElements()) {
            executeCartRead(request, response);
        }

        else {
            Cart cart = getParameterSetCartObject(request);
            ProfileResponse profileResponse = fProfileService.getProfile(principalDetails.getUsername());

            long discountRate = Membership.findByCodeName(profileResponse.getGradeName()).getDiscountRate();
            long discountPrice = Math.round(cart.totalPrice * (100-discountRate) / 100.0);

            CartResponse cartResponse = CartResponse.of(cart.itemName, cart.itemCounting, cart.totalPrice, discountPrice);

            Cookie cookie = executeSetCookie(cart, cartResponse);
            response.addCookie(cookie);

        }

    }

    private void executeCartRead(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getCookies() == null) {
            response.getWriter().write(URLDecoder.decode(COOKIE_EMPTY_STRING, "UTF-8"));
        }

        else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie: cookies) {
                response.getWriter().write(cookie.getName() + " " + URLDecoder.decode(cookie.getValue(), "UTF-8"));
            }
        }

    }

    private Cart getParameterSetCartObject(HttpServletRequest request) {

        long itemSeq = Long.parseLong(request.getParameter(COOKIE_PARAMETER_SEQ));
        long itemCounting = Long.parseLong(request.getParameter(COOKIE_PARAMETER_COUNT));

        ItemResponse item = fItemService.getItemDetail(itemSeq);

        long totalPrice = item.getPrice() * itemCounting;

        return new Cart(itemSeq, itemCounting, item.getItemName(), totalPrice);
    }

    private Cookie executeSetCookie(Cart cart, CartResponse cartResponse) throws UnsupportedEncodingException {

        Cookie cookie = new Cookie(String.valueOf(cart.itemSeq), URLEncoder.encode(cartResponse.toString(), "UTF-8"));
        cookie.setMaxAge(3600 * 12);
        cookie.setPath("/");
        return cookie;
    }

    static class Cart {

        long itemSeq;
        long itemCounting;
        String itemName;
        long totalPrice;

        public Cart(long itemSeq, long itemCounting, String itemName, long totalPrice) {
            this.itemSeq = itemSeq;
            this.itemCounting = itemCounting;
            this.itemName = itemName;
            this.totalPrice = totalPrice;
        }
    }
}
