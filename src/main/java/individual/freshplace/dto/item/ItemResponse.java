package individual.freshplace.dto.item;

import individual.freshplace.entity.Image;
import individual.freshplace.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ItemResponse {

    private final String itemName;

    private final String description;

    private final long weight;

    private final long price;

    private final String origin;

    private final LocalDate expirationDate;

    private final String category;

    private final List<String> imageUrlList;

    public static ItemResponse from(final Item item, final List<Image> images) {
        return new ItemResponse(
                item.getItemName(),
                item.getDescription(),
                item.getWeight(),
                item.getPrice(),
                item.getOrigin(),
                item.getExpirationDate(),
                item.getCategoryCode().getCodeKorName(),
                images.stream().map(image -> image.getImagePath()).collect(Collectors.toList()));
    }
}
