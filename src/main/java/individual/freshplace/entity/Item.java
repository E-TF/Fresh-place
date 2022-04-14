package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemSeq;

    private String itemName;

    private String description;

    @Column(columnDefinition = "text")
    private String image;

    private long weight;

    private String origin;

    private long inventory;

    private long salesQuantity;

    private LocalDate expirationDate;

    private String categoryCode;
}
