package individual.freshplace.dto.item;

import individual.freshplace.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemByCategoryResponse {

    private String itemName;

    private long price;

    public static ItemByCategoryResponse from(final Item item) {
        return new ItemByCategoryResponse(
                item.getItemName(),
                item.getPrice());
    }
}
