package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cart_detail")
@IdClass(Cart.CartId.class)
@Getter @Setter
public class Cart extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "item_seq")
    private Item itemId;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_seq")
    private Member memberId;

    @Column(name = "item_counting")
    private long count;

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class CartId implements Serializable {

        private Long itemId;

        private Long memberId;

    }

}
