package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.order.OrderDetailResponse;
import individual.freshplace.dto.order.OrderRequest;
import individual.freshplace.dto.order.OrderResponse;
import individual.freshplace.dto.payment.CancellationReceipt;
import individual.freshplace.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public @ResponseBody List<OrderResponse> getOrdersInformation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return orderService.getOrders(principalDetails.getUsername());
    }

    @GetMapping("/{orderSeq}")
    public @ResponseBody OrderDetailResponse getOrderDetailsInformation(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long orderSeq) {
        return orderService.getOrderDetailsAndPayment(orderSeq);
    }

    @PostMapping
    public String orderAndPaymentRequest(HttpServletRequest httpServletRequest, @AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody OrderRequest orderRequest) {
        return "redirect:" + orderService.addOrderAndGetPaymentRedirectUrl(principalDetails.getUsername(), orderRequest, httpServletRequest.getCookies());
    }

    @PatchMapping("/{orderSeq}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody CancellationReceipt orderAndPaymentCancelRequest(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long orderSeq) {
        return orderService.cancelOrderAndPaymentAndChangeStock(orderSeq);
    }
}
