package individual.freshplace.controller;

import individual.freshplace.config.auth.PrincipalDetails;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressAddRequest;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressResponse;
import individual.freshplace.dto.deliveryaddress.DeliveryAddressUpdateRequest;
import individual.freshplace.service.DeliveryAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/delivery-address")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @GetMapping
    public List<DeliveryAddressResponse> getDeliveryAddresses(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return deliveryAddressService.getDeliveryAddresses(principalDetails.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryAddressResponse addDeliveryAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressAddRequest deliverAddressAddRequest) {
        return deliveryAddressService.addDeliveryAddress(principalDetails.getUsername(), deliverAddressAddRequest);
    }

    @PatchMapping("/zipcode")
    public DeliveryAddressResponse updateDeliveryAddressZipCode(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.ZipCode zipCode) {
        return deliveryAddressService.updateDeliveryAddressZipCode(principalDetails.getUsername(), zipCode);
    }

    @PatchMapping("/address")
    public DeliveryAddressResponse updateDeliveryAddressDetailAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Address address) {
        return deliveryAddressService.updateDeliveryAddressDetailAddress(principalDetails.getUsername(), address);
    }

    @PatchMapping("/recipient")
    public DeliveryAddressResponse updateDeliveryAddressRecipient(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Recipient recipient) {
        return deliveryAddressService.updateDeliveryAddressRecipient(principalDetails.getUsername(), recipient);
    }

    @PatchMapping("/contact")
    public DeliveryAddressResponse updateDeliveryAddressContact(@AuthenticationPrincipal PrincipalDetails principalDetails, @Valid @RequestBody DeliveryAddressUpdateRequest.Contact contact) {
        return deliveryAddressService.updateDeliveryAddressContact(principalDetails.getUsername(), contact);
    }

    @DeleteMapping("/{deliverSeq}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeliveryAddress(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long deliverSeq) {
        deliveryAddressService.deleteDeliveryAddress(principalDetails.getUsername(), deliverSeq);
    }
}
