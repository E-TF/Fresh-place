package individual.freshplace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_seq")
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String contents;

    private Long views;

    private Long rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_seq")
    private OrderDetail orderDetail;
}
