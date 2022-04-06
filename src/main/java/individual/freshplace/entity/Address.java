package individual.freshplace.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String zipCode;

    private String address;
}
