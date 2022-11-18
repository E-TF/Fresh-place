package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.cart.CartResponse;
import individual.freshplace.dto.kakaopay.KakaoPayApprovalResponse;
import individual.freshplace.dto.kakaopay.PayView;
import individual.freshplace.dto.order.OrderItem;
import individual.freshplace.service.FCartReadService;
import individual.freshplace.util.payment.KakaoPay;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final KakaoPay kakaoPay;
    private final FCartReadService fCartReadService;

    //결제 준비 요청
    @PostMapping("/members/payment")
    public ResponseEntity<PayView> paymentRequest(HttpServletRequest httpServletRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Cookie[] cookies = httpServletRequest.getCookies();
        CartResponse cartResponses = fCartReadService.getCartByMember(principalDetails.getUsername(), cookies);
        List<OrderItem> orderItems = cartResponses.getCartItems().stream().map(cartResponse -> OrderItem.of(cartResponse.getItemName(), cartResponse.getItemCounting(), cartResponse.getDiscountPrice())).collect(Collectors.toList());
        return ResponseEntity.ok().body(kakaoPay.getKakaoPayReadyResponse(principalDetails.getUsername(), orderItems));
    }

    //결제 승인 요청
    @GetMapping("/kakaopaySuccess")
    public ResponseEntity<KakaoPayApprovalResponse> kakaoPaySuccess(@RequestParam("pg_token") String pgToken, @Valid @RequestBody PayView payView) {
        return ResponseEntity.ok().body(kakaoPay.getKakaoPaymentInformation(pgToken, payView));
    }
}
