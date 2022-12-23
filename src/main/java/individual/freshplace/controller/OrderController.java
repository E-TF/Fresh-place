package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.cart.CartItem;
import individual.freshplace.dto.cart.CartResponse;
import individual.freshplace.dto.order.OrderRequest;
import individual.freshplace.service.FCartReadService;
import individual.freshplace.service.FOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final FOrderService fOrderService;

    @PostMapping("/members/order")
    public String orderAndPaymentRequest(HttpServletRequest httpServletRequest, @AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody OrderRequest orderRequest) {
        return "redirect:" + fOrderService.addOrderAndGetPaymentRedirectUrl(principalDetails.getUsername(), orderRequest, httpServletRequest.getCookies());
    }
}
