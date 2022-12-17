package individual.freshplace.controller;

import individual.freshplace.dto.payment.Receipt;
import individual.freshplace.service.FOrderService;
import individual.freshplace.util.PrincipalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final FOrderService fOrderService;

    @GetMapping("/kakaopaySuccess")
    @ResponseStatus(HttpStatus.CREATED)
    public Receipt kakaoPaySuccess(@RequestParam("pg_token") String pgToken) {
        return fOrderService.addPayment(pgToken, PrincipalUtils.getUsername());
    }
}
