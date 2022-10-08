package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "cart_detail")
public class Cart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartSeq;

    @ManyToOne
    @JoinColumn(name = "item_seq")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "member_seq")
    private Member member;

    private long itemCounting;
}
