package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cart_detail")
@Getter @Setter
public class Cart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_seq")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "member_seq")
    private Member member;

    @Column(name = "item_counting")
    private long count;
}
