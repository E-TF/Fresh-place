package individual.freshplace.dto.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import individual.freshplace.entity.Image;
import individual.freshplace.entity.Item;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemResponse {

    private final String itemName;

    private final String description;

    private final long weight;

    private final long price;

    private final String origin;

    private final LocalDate expirationDate;

    private final String category;

    private final List<String> imageUrlList;

    @JsonCreator
    private ItemResponse(@JsonProperty("itemName") String itemName, @JsonProperty("description") String description,
                         @JsonProperty("weight") long weight, @JsonProperty("price") long price,
                         @JsonProperty("origin") String origin, @JsonSerialize(using = LocalDateSerializer.class)
                             @JsonDeserialize(using = LocalDateDeserializer.class) @JsonProperty("expirationDate") LocalDate expirationDate,
                         @JsonProperty("category") String category, @JsonProperty("imageUrlList") List<String> imageUrlList) {

        this.itemName = itemName;
        this.description = description;
        this.weight = weight;
        this.price = price;
        this.origin = origin;
        this.expirationDate = expirationDate;
        this.category = category;
        this.imageUrlList = imageUrlList;
    }

    public static ItemResponse of(final Item item, final List<Image> images) {
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
