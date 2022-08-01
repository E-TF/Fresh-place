package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliverAddress extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliverSeq;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    private Member member;

    private String recipient;

    private String contact;

    @Builder
    public DeliverAddress(Address address, Member member, String recipient, String contact) {
        this.address = address;
        this.member = member;
        this.recipient = recipient;
        this.contact = contact;
    }

    public void updateRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void updateContact(String contact) {
        this.contact = contact;
    }
}
