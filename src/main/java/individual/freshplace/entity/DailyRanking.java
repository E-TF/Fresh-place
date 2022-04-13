package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
public class DailyRanking extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_seq")
    private Item item;

    private long ranking;
}
