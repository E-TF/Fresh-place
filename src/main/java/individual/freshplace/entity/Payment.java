package individual.freshplace.entity;

import individual.freshplace.util.constant.code.payment.PaymentStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentSeq;

    private long paymentAmount;

    @Column(name = "payment_status_code")
    private PaymentStatus statusCode;

    private String paymentMethod;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_seq")
    private Order order;

    private LocalDateTime paymentDate;

    private String paymentTid;

    @Builder
    public Payment(final long paymentAmount, final String paymentMethod, final Order order, final LocalDateTime paymentDate, final String paymentTid) {
        this.paymentAmount = paymentAmount;
        this.statusCode = PaymentStatus.SUCCESS;
        this.paymentMethod = paymentMethod;
        this.order = order;
        this.paymentDate = paymentDate;
        this.paymentTid = paymentTid;
    }
}
