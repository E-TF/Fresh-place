package individual.freshplace.entity;

import individual.freshplace.util.constant.CategoryType;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemSeq;

    private String itemName;

    private String description;

    private long price;

    private long weight;

    private String origin;

    private long inventory;

    private long salesQuantity;

    private LocalDate expirationDate;

//    @Convert(converter = CategoryTypeConverter.class)
    private CategoryType categoryCode;
}
