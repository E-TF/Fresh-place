package individual.freshplace.dto.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import individual.freshplace.entity.Item;
import lombok.Getter;

@Getter
public class ItemByCategoryResponse {

    private final long itemSeq;
    private final String itemName;
    private final long price;
    private final String thumbNailImage;

    @JsonCreator
    private ItemByCategoryResponse(@JsonProperty("itemSeq") long itemSeq, @JsonProperty("itemName") String itemName,
                                   @JsonProperty("price") long price, @JsonProperty("thumbNailImage") String thumbNailImage) {

        this.itemSeq = itemSeq;
        this.itemName = itemName;
        this.price = price;
        this.thumbNailImage = thumbNailImage;
    }

    public ItemByCategoryResponse(final Item item, final String thumbNailImage) {
        this.itemSeq = item.getItemSeq();
        this.itemName = item.getItemName();
        this.price = item.getPrice();
        this.thumbNailImage = thumbNailImage;
    }
}
