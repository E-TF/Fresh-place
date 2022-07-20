package individual.freshplace.dto.deliveryaddress;

import individual.freshplace.entity.Address;
import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DeliveryAddressAddRequest {

    @Column
    @NotBlank
    @Length(min = 5, max = 5)
    private String zipCode;

    @NotBlank
    private String address;

    @NotBlank
    @Length(min = 2, message = "수취인 명은 2글자 이상이어야 합니다.")
    private String recipient;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")
    private String contact;

    public DeliverAddress toDeliverAddress(final Member member) {
        return DeliverAddress.builder()
                .address(Address.builder().zipCode(zipCode).address(address).build())
                .member(member)
                .recipient(recipient)
                .contact(contact)
                .build();
    }
}
