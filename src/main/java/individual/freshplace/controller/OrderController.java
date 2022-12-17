package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.order.OrderDetailResponse;
import individual.freshplace.dto.order.OrderRequest;
import individual.freshplace.dto.order.OrderResponse;
import individual.freshplace.service.FOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/order")
public class OrderController {

    private final FOrderService fOrderService;

    @GetMapping
    public @ResponseBody List<OrderResponse> getOrdersInformation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return fOrderService.getOrders(principalDetails.getUsername());
    }

    @GetMapping("/{orderSeq}")
    public @ResponseBody OrderDetailResponse getOrderDetailsInformation(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long orderSeq) {
        return fOrderService.getOrderDetailsAndPayment(orderSeq);
    }

    @PostMapping
    public String orderAndPaymentRequest(HttpServletRequest httpServletRequest, @AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody OrderRequest orderRequest) {
        return "redirect:" + fOrderService.addOrderAndGetPaymentRedirectUrl(principalDetails.getUsername(), orderRequest, httpServletRequest.getCookies());
    }
}
