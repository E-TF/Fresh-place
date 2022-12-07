package individual.freshplace.controller;

import individual.freshplace.dto.payment.Receipt;
import individual.freshplace.service.FOrderService;
import individual.freshplace.util.PrincipalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final FOrderService fOrderService;

    @GetMapping("/kakaopaySuccess")
    public ResponseEntity<Receipt> kakaoPaySuccess(@RequestParam("pg_token") String pgToken) {
        return ResponseEntity.ok().body(fOrderService.addPayment(pgToken, PrincipalUtils.getUsername()));
    }
}
