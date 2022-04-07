package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String zipCode;
    private String address;

    public Address() {
    }

    public Address(String zipCode, String address) {
        this.zipCode = zipCode;
        this.address = address;
    }
}
