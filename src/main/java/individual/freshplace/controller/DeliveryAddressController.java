package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressUpdateRequest;
import individual.freshplace.service.FDeliveryAddressService;
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

    private final FDeliveryAddressService fDeliveryAddressService;

    @GetMapping
    public ResponseEntity<List<DeliveryAddressResponse>> getDeliveryAddresses(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.ok().body(fDeliveryAddressService.getDeliveryAddresses(principalDetails.getUsername()));
    }

    @PostMapping("/add")
    public ResponseEntity<DeliveryAddressResponse> AddDeliveryAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressAddRequest deliverAddressAddRequest) {
        return ResponseEntity.ok().body(fDeliveryAddressService.addDeliveryAddress(principalDetails.getUsername(), deliverAddressAddRequest));
    }

    @PatchMapping("/zipcode")
    public ResponseEntity<DeliveryAddressResponse> updateDeliveryAddressZipCode(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.ZipCode zipCode) {
        return ResponseEntity.ok().body(fDeliveryAddressService.updateDeliveryAddressZipCode(principalDetails.getUsername(), zipCode));
    }

    @PatchMapping("/address")
    public ResponseEntity<DeliveryAddressResponse> updateDeliveryAddressDetailAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Address address) {
        return ResponseEntity.ok().body(fDeliveryAddressService.updateDeliveryAddressDetailAddress(principalDetails.getUsername(), address));
    }

    @PatchMapping("/recipient")
    public ResponseEntity<DeliveryAddressResponse> updateDeliveryAddressRecipient(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Recipient recipient) {
        return ResponseEntity.ok().body(fDeliveryAddressService.updateDeliveryAddressRecipient(principalDetails.getUsername(), recipient));
    }

    @PatchMapping("/contact")
    public ResponseEntity<DeliveryAddressResponse> updateDeliveryAddressContact(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Contact contact) {
        return ResponseEntity.ok().body(fDeliveryAddressService.updateDeliveryAddressContact(principalDetails.getUsername(), contact));
    }
}
