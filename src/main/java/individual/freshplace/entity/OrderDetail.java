package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class OrderDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_seq")
    private Long orderDetailSeq;

    @Column(name = "item_count")
    private long count;

    @Column(name = "total_price")
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_seq")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_seq")
    private Order order;

    @OneToOne(mappedBy = "orderDetail")
    private Review review;
}
