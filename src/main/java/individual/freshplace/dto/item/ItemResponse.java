package individual.freshplace.dto.item;

import individual.freshplace.entity.Image;
import individual.freshplace.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ItemResponse {

    private String itemName;

    private String description;

    private long weight;

    private long price;

    private String origin;

    private LocalDate expirationDate;

    private String category;

    private List<String> image;

    public static ItemResponse from(final Item item, final List<Image> images) {
        return new ItemResponse(
                item.getItemName(),
                item.getDescription(),
                item.getWeight(),
                item.getPrice(),
                item.getOrigin(),
                item.getExpirationDate(),
                item.getCategoryCode().getCodeName(),
                images.stream().map(image -> image.getImagePath()).collect(Collectors.toList()));
    }
}
