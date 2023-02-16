package individual.freshplace.dto.item;

import individual.freshplace.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ItemByCategoryResponse {

    private final long itemSeq;
    private final String itemName;
    private final long price;
    private final String thumbNailImage;

    public ItemByCategoryResponse(final Item item, final String thumbNailImage) {
        this.itemSeq = item.getItemSeq();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.thumbNailImage = thumbNailImage;
    }
}
