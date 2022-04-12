package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
public class DailyRanking extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_seq")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_seq")
    private Item item;

    private Long ranking;
}
