package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "orderDetail")
    private List<Review> review = new ArrayList<>();

    @Builder
    public OrderDetail(final long count, final long price, final Item item, final Order order) {
        this.count = count;
        this.price = price;
        this.item = item;
        this.order = order;
    }
}
