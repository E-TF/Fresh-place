package individual.freshplace.dto.deliveryaddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class DeliveryAddressUpdateRequest {

    @Getter
    @AllArgsConstructor
    public static class ZipCode {

        private Long deliverSeq;

        @NotBlank(message = "우편번호를 입력해주세요.")
        @Length(min = 5, max = 5, message = "5글자 입니다.")
        private String zipCode;
    }

    @Getter
    @AllArgsConstructor
    public static class Address {

        private Long deliverSeq;

        @NotBlank(message = "공백일 수 없습니다.")
        private String address;
    }

    @Getter
    @AllArgsConstructor
    public static class Recipient {

        private Long deliverSeq;

        @NotBlank
        @Length(min = 2, max = 5, message = "수취인 명은 2글자 이상 5글자 이하입니다.")
        private String recipient;
    }

    @Getter
    @AllArgsConstructor
    public static class Contact {

        private Long deliverSeq;

        @NotBlank
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "01*-****-**** 형식을 지켜주세요.")
        private String contact;
    }
}
