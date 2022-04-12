package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_seq")
    private Long id;

    private String itemName;

    private String description;

    @Column(columnDefinition = "text")
    private String image;

    private Long weight;

    private String origin;

    private Long inventory;

    private Long salesQuantity;

    private LocalDate expirationDate;

    private String categoryCode;
}
