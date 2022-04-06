package individual.freshplace.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_seq")
    private Long id;

    @Column(name = "item_name")
    private String name;

    private String description;

    @Column(columnDefinition = "text")
    private String image;

    private Long weight;

    private String origin;

    private Long inventory;

    private Long salesQuantity;

    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    @Column(name = "category_code")
    private String category;
}
