package individual.freshplace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_seq")
    private Long id;

    @Column(name = "payment_status_code")
    private String statusCode;

    @Column(name = "payment_method_code")
    private String methodCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_seq")
    private Order order;

    private LocalDateTime paymentDate;
}
