package individual.freshplace.entity;

import individual.freshplace.util.constant.code.payment.PaymentStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentSeq;

    private long paymentAmount;

    @Setter
    private PaymentStatus paymentStatusCode;

    private String paymentMethod;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_seq")
    private Order order;

    private String paymentTid;

    @Builder
    public Payment(final long paymentAmount, final String paymentMethod, final Order order, final String paymentTid) {
        this.paymentAmount = paymentAmount;
        this.paymentStatusCode = PaymentStatus.SUCCESS;
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.paymentTid = paymentTid;
    }
}
