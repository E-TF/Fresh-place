package individual.freshplace.controller;

import individual.freshplace.dto.payment.Receipt;
import individual.freshplace.service.OrderService;
import individual.freshplace.util.PrincipalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;

    @GetMapping("/kakaopaySuccess")
    @ResponseStatus(HttpStatus.CREATED)
    public Receipt kakaoPaySuccess(@RequestParam("pg_token") String pgToken) {
        return orderService.addPayment(pgToken, PrincipalUtils.getUsername());
    }
}
