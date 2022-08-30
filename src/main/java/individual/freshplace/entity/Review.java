package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewSeq;

    private String title;

    @Column(columnDefinition = "text")
    private String contents;

    private long views;

    private long rating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_seq")
    private OrderDetail orderDetail;
}
