package individual.freshplace.entity;

import individual.freshplace.util.constant.code.delivery.DeliveryStatus;
import individual.freshplace.util.constant.code.order.OrderStatus;
import individual.freshplace.util.constant.code.delivery.PlaceToReceive;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_seq")
    private Long orderSeq;

    private String orderName;

    @Setter
    private OrderStatus orderStatusCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    private Member member;

    @Embedded
    private Address address;

    private String receiverName;

    private String receiverPhoneNumber;

    private PlaceToReceive placeToReceiveCode;

    @Setter
    private DeliveryStatus deliveryStatusCode;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    @Builder
    public Order(final String orderName, final Member member, final Address address, final String receiverName, final String receiverPhoneNumber, final PlaceToReceive placeToReceiveCode) {
        this.orderName = orderName;
        this.orderStatusCode = OrderStatus.SUCCESS;
        this.member = member;
        this.address = address;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.placeToReceiveCode = placeToReceiveCode;
        this.deliveryStatusCode = DeliveryStatus.READY;
    }
}
