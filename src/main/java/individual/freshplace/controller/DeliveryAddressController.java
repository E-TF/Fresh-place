package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressUpdateRequest;
import individual.freshplace.service.FDeliveryAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public List<DeliveryAddressResponse> getDeliveryAddresses(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return fDeliveryAddressService.getDeliveryAddresses(principalDetails.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryAddressResponse addDeliveryAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressAddRequest deliverAddressAddRequest) {
        return fDeliveryAddressService.addDeliveryAddress(principalDetails.getUsername(), deliverAddressAddRequest);
    }

    @PatchMapping("/zipcode")
    public DeliveryAddressResponse updateDeliveryAddressZipCode(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.ZipCode zipCode) {
        return fDeliveryAddressService.updateDeliveryAddressZipCode(principalDetails.getUsername(), zipCode);
    }

    @PatchMapping("/address")
    public DeliveryAddressResponse updateDeliveryAddressDetailAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Address address) {
        return fDeliveryAddressService.updateDeliveryAddressDetailAddress(principalDetails.getUsername(), address);
    }

    @PatchMapping("/recipient")
    public ResponseEntity<DeliveryAddressResponse> updateDeliveryAddressRecipient(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Recipient recipient) {
        return ResponseEntity.ok().body(fDeliveryAddressService.updateDeliveryAddressRecipient(principalDetails.getUsername(), recipient));
    }

    @PatchMapping("/contact")
    public DeliveryAddressResponse updateDeliveryAddressContact(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Contact contact) {
        return fDeliveryAddressService.updateDeliveryAddressContact(principalDetails.getUsername(), contact);
    }

    @DeleteMapping("/{deliverSeq}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeliveryAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long deliverSeq) {
        fDeliveryAddressService.deleteDeliveryAddress(principalDetails.getUsername(), deliverSeq);
    }
}
