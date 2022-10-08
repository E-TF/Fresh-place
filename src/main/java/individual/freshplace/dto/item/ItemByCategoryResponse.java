package individual.freshplace.dto.item;

import individual.freshplace.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ItemByCategoryResponse {

    private final String itemName;

    private final long price;

    public static ItemByCategoryResponse from(final Item item) {
        return new ItemByCategoryResponse(
                item.getItemName(),
                item.getPrice());
    }
}
