package individual.freshplace.dto.deliveryaddress;

import individual.freshplace.entity.Address;
import individual.freshplace.entity.DeliverAddress;
import individual.freshplace.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class DeliveryAddressAddRequest {

    @NotBlank(message = "우편번호를 입력해주세요.")
    @Length(min = 5, max = 5, message = "5글자 입니다.")
    private String zipCode;

    @NotBlank(message = "공백일 수 없습니다.")
    private String address;

    @NotBlank
    @Length(min = 2, max = 5, message = "수취인 명은 2글자 이상 5글자 이하입니다.")
    private String recipient;

    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "01*-****-**** 형식을 지켜주세요.")
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
