package individual.freshplace.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@IdClass(DailyRanking.RankingId.class)
@Getter @Setter
public class DailyRanking {

    @Id
    @OneToOne
    @JoinColumn(name = "item_seq")
    private Item item;

    @Id
    @Column(name = "date_by_day")
    private LocalDateTime day;

    private Long ranking;


    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class RankingId implements Serializable {

        private Long item;

        private LocalDateTime day;

    }

}
