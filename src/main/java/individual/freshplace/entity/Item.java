package individual.freshplace.entity;

import individual.freshplace.dto.item.ItemUpdateRequest;
import individual.freshplace.util.constant.code.category.SubCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

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

    private SubCategory categoryCode;

    public void updateItem(final ItemUpdateRequest itemUpdateRequest) {
        this.itemName = itemUpdateRequest.getItemName();
        this.description = itemUpdateRequest.getDescription();
        this.price = itemUpdateRequest.getPrice();
        this.weight = itemUpdateRequest.getWeight();
        this.origin = itemUpdateRequest.getOrigin();
        this.expirationDate = itemUpdateRequest.getExpirationDate();
        this.categoryCode = SubCategory.findByCodeEngName(itemUpdateRequest.getCategoryEngName());
    }

    public void decreaseInventoryAndIncreaseSalesQuantity(final long purchaseQuantity) {
        this.inventory -= purchaseQuantity;
        this.salesQuantity += purchaseQuantity;
    }

    public void increaseInventoryForOrderCancel(final long purchaseQuantity) {
        this.inventory += purchaseQuantity;
    }

    @Builder
    public Item(Long itemSeq, String itemName, String description, long price, long weight, String origin, long inventory,
                SubCategory subCategory) {
        this.itemSeq = itemSeq;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.origin = origin;
        this.inventory = inventory;
        this.salesQuantity = 0;
        this.categoryCode = subCategory;
    }
}
