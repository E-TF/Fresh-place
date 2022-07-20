package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.entity.Member;
import individual.freshplace.service.DeliveryAddressService;
import individual.freshplace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/delivery-address")
public class DeliveryAddressController {

    private final MemberService memberService;
    private final DeliveryAddressService deliverAddressService;

    @GetMapping
    public ResponseEntity<List<DeliveryAddressResponse>> getDeliveryAddresses(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Member member = memberService.findMember(principalDetails.getUsername());
        return ResponseEntity.ok().body(deliverAddressService.getDeliveryAddresses(member));
    }

    @PostMapping("/add")
    public ResponseEntity<DeliveryAddressResponse> AddDeliveryAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressAddRequest deliverAddressAddRequest) {
        final Member member = memberService.findMember(principalDetails.getUsername());
        return ResponseEntity.ok().body(deliverAddressService.AddDeliveryAddress(member, deliverAddressAddRequest));
    }
}
