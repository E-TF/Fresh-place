package individual.freshplace.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String zipCode;
    private String address;

    @Builder
    public Address(String zipCode, String address) {
        this.zipCode = zipCode;
        this.address = address;
    }

    public void updateZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void updateAddress(String address) {
        this.address = address;
    }
}
