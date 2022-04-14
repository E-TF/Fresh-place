package individual.freshplace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentSeq;

    @Column(name = "payment_status_code")
    private String statusCode;

    @Column(name = "payment_method_code")
    private String methodCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_seq")
    private Order order;

    private LocalDateTime paymentDate;
}
